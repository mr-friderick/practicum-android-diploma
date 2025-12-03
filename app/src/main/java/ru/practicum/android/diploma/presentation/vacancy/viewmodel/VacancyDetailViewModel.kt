package ru.practicum.android.diploma.presentation.vacancy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.favourites.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyInteractor

class VacancyDetailViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val favoriteInteractor: FavoriteVacancyInteractor
) : ViewModel() {

    private var isFavorite = false
    private var model: VacancyDetailModel? = null
    private val _state = MutableLiveData<VacancyDetailViewState>()
    val state: LiveData<VacancyDetailViewState> = _state

    // Добавляем LiveData для контактных интентов
    private val _contactIntent = MutableLiveData<ContactIntent?>()
    val contactIntent: LiveData<ContactIntent?> = _contactIntent

    sealed class ContactIntent {
        data class Email(val email: String) : ContactIntent()
        data class Phone(val phoneNumber: String, val comment: String?) : ContactIntent()
    }

    // Методы для обработки контактов
    fun onEmailClicked(email: String) {
        _contactIntent.value = ContactIntent.Email(email)
    }

    fun onPhoneClicked(phoneNumber: String, comment: String?) {
        _contactIntent.value = ContactIntent.Phone(phoneNumber, comment)
    }

    // Метод для сброса intent после обработки
    fun onContactIntentHandled() {
        _contactIntent.value = null
    }

    fun searchVacancyDetail(id: String) {
        viewModelScope.launch {
            _state.postValue(VacancyDetailViewState.Loading)

            vacancyInteractor.searchVacancyDetail(id).collect { state ->
                when (state) {
                    is VacancySearchState.VacancyDetail -> {
                        model = state.vacancyDetail
                        isFavorite = favoriteInteractor.isFavorite(id)
                        _state.postValue(VacancyDetailViewState.VacancyDetail(state.vacancyDetail, isFavorite))
                    }

                    is VacancySearchState.NotFound -> {
                        _state.postValue(VacancyDetailViewState.NotFound)
                    }

                    is VacancySearchState.NoInternet -> {
                        // Попытка загрузить из избранного
                        val cachedVacancy = favoriteInteractor.getById(id)
                        if (cachedVacancy != null) {
                            model = cachedVacancy
                            isFavorite = true
                            _state.postValue(VacancyDetailViewState.VacancyDetail(cachedVacancy, true))
                        } else {
                            _state.postValue(VacancyDetailViewState.NoInternet)
                        }
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

    fun favoriteControl() {
        val current = model ?: return

        viewModelScope.launch {
            isFavorite = if (isFavorite) {
                favoriteInteractor.delete(current.id)
                false
            } else {
                favoriteInteractor.add(current)
                true
            }

            _state.postValue(
                VacancyDetailViewState.VacancyDetail(current, isFavorite)
            )
        }
    }

    fun getShareUrl(): String? {
        return model?.url
    }
}
