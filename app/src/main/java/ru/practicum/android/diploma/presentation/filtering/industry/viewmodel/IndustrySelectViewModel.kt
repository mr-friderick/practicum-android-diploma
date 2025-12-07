package ru.practicum.android.diploma.presentation.filtering.industry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState

class IndustrySelectViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableLiveData<IndustryViewState>()
    val state: MutableLiveData<IndustryViewState> = _state

    // Для хранения списка всех отраслей
    private val _allIndustries = MutableStateFlow<List<FilterIndustryModel>>(emptyList())

    // Для хранения отфильтрованного списка
    private val _filteredIndustries = MutableStateFlow<List<FilterIndustryModel>>(emptyList())
    val filteredIndustries: StateFlow<List<FilterIndustryModel>> = _filteredIndustries.asStateFlow()

    // Для хранения выбранной отрасли
    private val _selectedIndustry = MutableStateFlow<FilterIndustryModel?>(null)
    val selectedIndustry: StateFlow<FilterIndustryModel?> = _selectedIndustry.asStateFlow()

    // Для хранения текста поиска
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    fun searchIndustries() {
        viewModelScope.launch {
            _state.postValue(IndustryViewState.Loading)

            filterInteractor.searchIndustries().collect { state ->
                when (state) {
                    is SearchState.Success<List<FilterIndustryModel>> -> {
                        _allIndustries.value = state.data
                        _filteredIndustries.value = state.data
                        _state.postValue(IndustryViewState.Industry(state.data))
                    }

                    is SearchState.NoInternet -> {
                        _state.postValue(IndustryViewState.NoInternet)
                    }

                    is SearchState.Error -> {
                        _state.postValue(IndustryViewState.Error(state.message))
                    }

                    else -> { /* Остальные стейты не требуют обработки */ }
                }
            }
        }
    }

    fun updateSearchText(text: String) {
        _searchText.value = text
        filterIndustries(text)
    }

    fun selectIndustry(industry: FilterIndustryModel?) {
        _selectedIndustry.value = industry
    }

    fun getSelectedIndustry(): FilterIndustryModel? {
        return _selectedIndustry.value
    }

    private fun filterIndustries(query: String) {
        if (query.isEmpty()) {
            _filteredIndustries.value = _allIndustries.value
        } else {
            val filtered = _allIndustries.value.filter { industry ->
                industry.name.contains(query, ignoreCase = true)
            }
            _filteredIndustries.value = filtered
        }
    }
}
