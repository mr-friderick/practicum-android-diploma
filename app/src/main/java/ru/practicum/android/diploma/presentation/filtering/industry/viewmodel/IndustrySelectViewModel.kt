package ru.practicum.android.diploma.presentation.filtering.industry.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState

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
                    is SearchState.Success<List<FilterIndustryModel>> -> {
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

}
