package ru.practicum.android.diploma.domain.models

sealed interface VacancySearchState {
    data class Content(val vacancy: VacancyModel) : VacancySearchState
    object NoAuth : VacancySearchState
    object NotFound : VacancySearchState
    object NoInternet : VacancySearchState
    object Error : VacancySearchState
}
