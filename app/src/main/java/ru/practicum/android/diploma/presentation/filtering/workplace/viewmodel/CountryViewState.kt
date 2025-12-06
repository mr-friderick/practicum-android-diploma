package ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel

import ru.practicum.android.diploma.domain.models.FilterAreaModel

sealed interface CountryViewState {
    object Loading : CountryViewState
    data class Country(val countries: List<FilterAreaModel>) : CountryViewState
    object NoInternet : CountryViewState
    data class Error(val message: String) : CountryViewState
}
