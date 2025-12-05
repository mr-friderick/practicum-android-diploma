package ru.practicum.android.diploma.presentation.filtering.industry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.VacancySearchState

class IndustrySelectViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableLiveData<IndustryViewState>()
    val state: MutableLiveData<IndustryViewState> = _state

    fun searchIndustries() {
        viewModelScope.launch {
            _state.postValue(IndustryViewState.Loading)

            filterInteractor.searchIndustries().collect { state ->
                when (state) {
                    is VacancySearchState.Error -> TODO()
                    is VacancySearchState.Industry -> TODO()
                    is VacancySearchState.NoInternet -> TODO()
                    is VacancySearchState.NotFound -> TODO()
                    else -> {

                    }
                }
            }
        }
    }

}
