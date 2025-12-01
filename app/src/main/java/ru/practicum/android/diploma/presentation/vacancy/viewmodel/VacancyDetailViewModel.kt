package ru.practicum.android.diploma.presentation.vacancy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyInteractor

class VacancyDetailViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    private val _state = MutableLiveData<VacancyDetailViewState>()
    val state: LiveData<VacancyDetailViewState> = _state

    fun searchVacancyDetail(id: String) {
        viewModelScope.launch {
            _state.postValue(VacancyDetailViewState.Loading)

            vacancyInteractor.searchVacancyDetail(id).collect { state ->
                when (state) {
                    is VacancySearchState.VacancyDetail -> {
                        _state.postValue(VacancyDetailViewState.VacancyDetail(state.vacancyDetail))
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
}
