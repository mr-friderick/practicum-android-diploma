package ru.practicum.android.diploma.domain.filtering

import ru.practicum.android.diploma.domain.models.FilterModel

interface FilterRepository {
    suspend fun getFilter(): FilterModel?
    suspend fun saveFilter(filter: FilterModel)
    suspend fun clearFilter()
}

