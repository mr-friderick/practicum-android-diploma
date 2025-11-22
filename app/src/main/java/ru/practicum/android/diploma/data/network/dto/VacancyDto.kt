package ru.practicum.android.diploma.data.network.dto

data class VacancyDto(
    val found: Int,
    val pages: Int,
    val page: Int,
    val vacancies: List<VacancyDetailDto>
)
