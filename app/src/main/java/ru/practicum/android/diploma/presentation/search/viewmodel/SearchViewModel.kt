package ru.practicum.android.diploma.presentation.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyInteractor
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    private val _state = MutableLiveData<SearchViewState>(SearchViewState.Default)
    val state: LiveData<SearchViewState> = _state

    fun setDefaultState() {
        _state.postValue(SearchViewState.Default)
    }

    fun searchVacancy(
        text: String,
        page: Int,
        filter: FilterModel?
    ) {
        debounce<Unit>(
            SEARCH_DELAY,
            viewModelScope,
            true
        ) {
            _state.postValue(SearchViewState.Loading)

            vacancyInteractor.searchVacancy(text, page, filter).collect { state ->
                when (state) {
                    is VacancySearchState.Content -> {
                        _state.postValue(SearchViewState.Vacancy(state.vacancy))
                    }
                    is VacancySearchState.NotFound -> {
                        _state.postValue(SearchViewState.NotFound)
                    }
                    else -> {
                        _state.postValue(SearchViewState.Error)
                    }
                }
            }
        }
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
