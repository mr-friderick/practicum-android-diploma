package ru.practicum.android.diploma.presentation.filtering.industry.viewmodel

import ru.practicum.android.diploma.domain.models.FilterIndustryModel

sealed interface IndustryViewState{
    object Loading : IndustryViewState
    data class Industry(
        val industries: List<FilterIndustryModel>) : IndustryViewState
    object NotFound : IndustryViewState
    object NoInternet : IndustryViewState
    data class Error(val message: String) : IndustryViewState
}
