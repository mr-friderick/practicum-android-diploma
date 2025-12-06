package ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.SearchState

class CountrySelectViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableLiveData<CountryViewState>()
    val state: MutableLiveData<CountryViewState> = _state

    fun searchCountries() {
        viewModelScope.launch {
            _state.postValue(CountryViewState.Loading)

            filterInteractor.searchCountries().collect { state ->
                when (state) {
                    is SearchState.Success<List<FilterAreaModel>> -> {
                        _state.postValue(CountryViewState.Country(state.data))
                    }

                    is SearchState.NoInternet -> {
                        _state.postValue(CountryViewState.NoInternet)
                    }

                    is SearchState.Error -> {
                        _state.postValue(CountryViewState.Error(state.message))
                    }

                    else -> { /* Остальные стейты не требуют обработки */ }
                }
            }
        }
    }
}
