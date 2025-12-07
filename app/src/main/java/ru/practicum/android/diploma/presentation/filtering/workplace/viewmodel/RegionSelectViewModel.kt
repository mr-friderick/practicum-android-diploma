package ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.SearchState

class RegionSelectViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableLiveData<RegionViewState>()
    val state: MutableLiveData<RegionViewState> = _state

    fun searchRegions() {
        viewModelScope.launch {
            _state.postValue(RegionViewState.Loading)

            filterInteractor.searchRegions().collect { state ->
                when (state) {
                    is SearchState.Success<List<FilterAreaModel>> -> {
                        _state.postValue(RegionViewState.Region(state.data))
                    }

                    is SearchState.NoInternet -> {
                        _state.postValue(RegionViewState.NoInternet)
                    }

                    is SearchState.Error -> {
                        _state.postValue(RegionViewState.Error(state.message))
                    }

                    else -> { /* Остальные стейты не требуют обработки */ }
                }
            }
        }
    }

}
