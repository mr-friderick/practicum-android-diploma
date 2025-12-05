package ru.practicum.android.diploma.domain.models

sealed interface VacancySearchState {
    data class VacancyDetail(val vacancyDetail: VacancyDetailModel) : VacancySearchState
    data class Industry(val industries: List<FilterIndustryModel>) : VacancySearchState
    object NotFound : VacancySearchState
    object NoInternet : VacancySearchState
    data class Error(val message: String) : VacancySearchState
}
