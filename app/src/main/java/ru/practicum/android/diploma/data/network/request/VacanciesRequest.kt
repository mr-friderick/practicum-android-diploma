package ru.practicum.android.diploma.data.network.request

sealed interface VacanciesRequest {
    data object Industries : VacanciesRequest
    data object Areas : VacanciesRequest
    data class VacancyDetail(
        val id: String
    ) : VacanciesRequest
    data class Vacancy(
        val area: Int?,
        val industry: Int?,
        val text: String?,
        val salary: Int?,
        val page: Int?,
        val onlyWithSalary: Boolean?
    ) : VacanciesRequest
}
