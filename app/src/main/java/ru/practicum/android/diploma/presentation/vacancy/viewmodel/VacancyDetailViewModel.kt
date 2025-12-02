package ru.practicum.android.diploma.presentation.vacancy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.database.entity.FavoriteVacancyEntity
import ru.practicum.android.diploma.data.repository.FavoriteRepository
import ru.practicum.android.diploma.domain.models.ContactsModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyInteractor
import android.util.Log
import java.io.IOException
import android.database.sqlite.SQLiteException

class VacancyDetailViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    companion object {
        private const val TAG = "VacancyDetailViewModel"
    }

    private val _state = MutableLiveData<VacancyDetailViewState>()
    val state: LiveData<VacancyDetailViewState> = _state

    private val _showShareSheet = MutableLiveData(false)
    val showShareSheet: LiveData<Boolean> = _showShareSheet

    private val _isFavorite = MutableLiveData(false)
    val isFavorite: LiveData<Boolean> = _isFavorite

    private var currentVacancyUrl: String? = null
    private var currentVacancy: VacancyDetailModel? = null

    fun searchVacancyDetail(id: String) {
        viewModelScope.launch {
            _state.postValue(VacancyDetailViewState.Loading)

            vacancyInteractor.searchVacancyDetail(id).collect { state ->
                when (state) {
                    is VacancySearchState.VacancyDetail -> {
                        currentVacancy = state.vacancyDetail
                        currentVacancyUrl = state.vacancyDetail.url
                        _state.postValue(VacancyDetailViewState.VacancyDetail(state.vacancyDetail))
                        checkIsFavorite(state.vacancyDetail.id)
                    }

                    is VacancySearchState.NotFound -> {
                        _state.postValue(VacancyDetailViewState.NotFound)
                    }

                    is VacancySearchState.NoInternet -> {
                        _state.postValue(VacancyDetailViewState.NoInternet)
                    }

                    else -> {
                        _state.postValue(
                            VacancyDetailViewState.Error((state as VacancySearchState.Error).message)
                        )
                    }
                }
            }
        }
    }

    private fun checkIsFavorite(id: String) {
        viewModelScope.launch {
            val fav = try {
                favoriteRepository.isFavorite(id)
            } catch (e: SQLiteException) {
                Log.e(TAG, "SQLite error checking if favorite: ${e.message}", e)
                false
            } catch (e: IOException) {
                Log.e(TAG, "IO error checking if favorite: ${e.message}", e)
                false
            } catch (e: Exception) {
                // Оставляем общий catch для непредвиденных исключений
                Log.e(TAG, "Unexpected error checking if favorite: ${e.message}", e)
                false
            }
            _isFavorite.postValue(fav)
        }
    }

    fun onShareClicked() {
        _showShareSheet.value = true
    }

    fun onShareSheetDismissed() {
        _showShareSheet.value = false
    }

    fun getShareUrl(): String? = currentVacancyUrl

    fun toggleFavorite() {
        viewModelScope.launch {
            currentVacancy?.let { vacancy ->
                val id = vacancy.id
                val isFav = try {
                    favoriteRepository.isFavorite(id)
                } catch (e: SQLiteException) {
                    Log.e(TAG, "SQLite error in toggleFavorite: ${e.message}", e)
                    false
                } catch (e: IOException) {
                    Log.e(TAG, "IO error in toggleFavorite: ${e.message}", e)
                    false
                } catch (e: Exception) {
                    Log.e(TAG, "Unexpected error in toggleFavorite: ${e.message}", e)
                    false
                }

                if (isFav) {
                    try {
                        favoriteRepository.removeById(id)
                        _isFavorite.postValue(false)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error removing favorite: ${e.message}", e)
                    }
                } else {
                    try {
                        favoriteRepository.add(mapToEntity(vacancy))
                        _isFavorite.postValue(true)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error adding favorite: ${e.message}", e)
                    }
                }
            }
        }
    }

    suspend fun addFavorite() {
        currentVacancy?.let {
            try {
                favoriteRepository.add(mapToEntity(it))
                _isFavorite.postValue(true)
            } catch (e: Exception) {
                Log.e(TAG, "Error in addFavorite: ${e.message}", e)
            }
        }
    }

    suspend fun removeFavorite() {
        currentVacancy?.let {
            try {
                favoriteRepository.removeById(it.id)
                _isFavorite.postValue(false)
            } catch (e: Exception) {
                Log.e(TAG, "Error in removeFavorite: ${e.message}", e)
            }
        }
    }

    private fun mapToEntity(v: ru.practicum.android.diploma.domain.models.VacancyDetailModel): FavoriteVacancyEntity {
        return FavoriteVacancyEntity(
            id = v.id,
            employerLogoUrl = v.employer.logo,
            vacancyName = v.name,
            region = v.address?.city ?: "",
            companyName = v.employer.name,
            salaryFrom = v.salary?.from,
            salaryTo = v.salary?.to,
            currency = v.salary?.currency,
            description = v.description,
            experience = v.experience?.name,
            schedule = v.schedule?.name,
            employment = v.employment?.name,
            contacts = v.contacts?.let { gsonContactsToJson(it) } ?: "",
            skills = v.skills.joinToString(separator = ","),
            vacancyUrl = v.url,
            industry = v.industry.name
        )
    }

    private fun gsonContactsToJson(contacts: ContactsModel): String {
        return "${contacts.email};${contacts.phones.joinToString { it.formatted }}"
    }
}
