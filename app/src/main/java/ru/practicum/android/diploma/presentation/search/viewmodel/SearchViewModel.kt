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

    private var currentPage: Int = 0
    private var maxPages: Int = 0

    private val debounceSearch = debounce<SearchParams>(
        SEARCH_DELAY,
        viewModelScope,
        true
    ) { params ->
        _state.postValue(SearchViewState.Loading)

        vacancyInteractor.searchVacancy(
            params.text,
            params.page,
            params.filter
        ).collect { state ->
            when (state) {
                is VacancySearchState.Content -> {
                    val vacancy = state.vacancy
                    currentPage = vacancy.page
                    maxPages = vacancy.pages

                    _state.postValue(SearchViewState.Vacancy(vacancy))
                }
                is VacancySearchState.NotFound -> {
                    _state.postValue(SearchViewState.NotFound)
                }
                is VacancySearchState.NoInternet -> {
                    _state.postValue(SearchViewState.NoInternet)
                }
                else -> {
                    _state.postValue(SearchViewState.Error)
                }
            }
        }
    }
    private val _state = MutableLiveData<SearchViewState>(SearchViewState.Default)
    val state: LiveData<SearchViewState> = _state

    fun setDefaultState() {
        _state.postValue(SearchViewState.Default)
    }

    fun searchVacancy(
        text: String,
        page: Int,
        filter: FilterModel? = null
    ) {
        debounceSearch(SearchParams(text, page, filter))
    }

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
