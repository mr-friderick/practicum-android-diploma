package ru.practicum.android.diploma.presentation.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingData.Companion.empty
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyInteractor
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    private var currentPage: Int = 0
    private var maxPages: Int = 0

    private val pagingParams = MutableStateFlow<PagingParams?>(null)
    private var currentFilter: FilterModel? = null
    
    private val searchTextDebounce = debounce<String>(
        SEARCH_DELAY,
        viewModelScope,
        true
    ) { text ->
        if (text.isNotBlank()) {
            pagingParams.update { PagingParams(text = text.trim(), filter = currentFilter) }
        } else {
            pagingParams.update { null }
        }
    }

    val vacanciesPaging: Flow<PagingData<VacancyDetailModel>> = pagingParams
        .flatMapLatest { params ->
            if (params == null) {
                flowOf(empty())
            } else {
                vacancyInteractor.observeVacanciesPaging(
                    text = params.text,
                    filter = params.filter
                )
            }
        }
        .cachedIn(viewModelScope)

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
                    _state.postValue(
                        SearchViewState.Error((state as VacancySearchState.Error).message)
                    )
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
        pagingParams.update { PagingParams(text = text, filter = filter) }
        debounceSearch(SearchParams(text, page, filter))
    }

    fun updateSearchText(text: String, filter: FilterModel? = null) {
        if (filter != null) {
            currentFilter = filter
        }
        if (text.isBlank()) {
            pagingParams.update { null }
        } else {
            searchTextDebounce(text)
        }
    }

    private data class PagingParams(
        val text: String,
        val filter: FilterModel? = null
    )

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
