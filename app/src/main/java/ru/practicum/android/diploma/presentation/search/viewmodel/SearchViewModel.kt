package ru.practicum.android.diploma.presentation.search.viewmodel

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
import ru.practicum.android.diploma.domain.search.VacancyInteractor
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacancyInteractor: VacancyInteractor
) : ViewModel() {

    private val pagingParams = MutableStateFlow<PagingParams?>(null)
    private var currentFilter: FilterModel? = null
    private val searchTextState = MutableStateFlow("")

    private val _isTyping = MutableStateFlow(false)
    val isTyping: kotlinx.coroutines.flow.StateFlow<Boolean> = _isTyping

    private val debounceSearch = debounce<String>(
        SEARCH_DELAY,
        viewModelScope,
        true
    ) { text ->
        _isTyping.value = false

        val trimmedText = text.trim()
        if (trimmedText.isNotBlank()) {
            pagingParams.update { PagingParams(text = trimmedText, filter = currentFilter) }
        } else {
            pagingParams.update { null }
        }
    }

    val vacanciesPaging: Flow<PagingData<VacancyDetailModel>> = pagingParams
        .flatMapLatest { params ->
            if (params == null) {
                flowOf(empty())
            } else {
                vacancyInteractor.searchVacancy(
                    text = params.text,
                    filter = params.filter
                )
            }
        }
        .cachedIn(viewModelScope)

    fun searchVacancy(text: String, filter: FilterModel? = null) {
        if (filter != null) {
            currentFilter = filter
        }

        searchTextState.value = text

        if (text.isBlank()) {
            _isTyping.value = false
            pagingParams.update { null }
        } else {
            _isTyping.value = true
            pagingParams.update { null }
            debounceSearch(text)
        }
    }

    fun getSearchText(): String {
        return searchTextState.value
    }

    private data class PagingParams(
        val text: String,
        val filter: FilterModel? = null
    )

    companion object {
        private const val SEARCH_DELAY = 2000L
    }
}
