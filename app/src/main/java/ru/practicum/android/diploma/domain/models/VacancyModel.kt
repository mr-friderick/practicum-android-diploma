package ru.practicum.android.diploma.domain.models

data class VacancyModel(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<VacancyDetailModel> = emptyList()
)
