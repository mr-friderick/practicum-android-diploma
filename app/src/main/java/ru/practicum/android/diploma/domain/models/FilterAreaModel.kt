package ru.practicum.android.diploma.domain.models

data class FilterAreaModel(
    val id: Int,
    val name: String,
    val parentId: Int?,
    val areas: List<FilterAreaModel> = emptyList()
)
