package ru.practicum.android.diploma.domain.models

sealed interface VacancySearchState {
    data class Vacancy(val vacancy: VacancyModel) : VacancySearchState
    data class VacancyDetail(val vacancyDetail: VacancyDetailModel) : VacancySearchState
    object NotFound : VacancySearchState
    object NoInternet : VacancySearchState
    data class Error(val message: String) : VacancySearchState
}
