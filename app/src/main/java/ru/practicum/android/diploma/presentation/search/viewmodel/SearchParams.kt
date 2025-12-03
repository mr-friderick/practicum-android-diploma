package ru.practicum.android.diploma.presentation.search.viewmodel

import ru.practicum.android.diploma.domain.models.FilterModel

data class SearchParams(
    val text: String,
    val page: Int,
    val filter: FilterModel? = null
)
