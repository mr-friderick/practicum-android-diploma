package ru.practicum.android.diploma.data.network.request

sealed interface VacanciesRequest {
    data object Industries : VacanciesRequest
    data object Areas : VacanciesRequest
    data class VacancyDetail(
        val id: String
    ) : VacanciesRequest
    data class Vacancy(
        // Фильтры
        val text: String,
        val page: Int,
        val area: Int? = null,
        val industry: Int? = null,
        val salary: Int? = null,
        val onlyWithSalary: Boolean? = null
    ) : VacanciesRequest
}
