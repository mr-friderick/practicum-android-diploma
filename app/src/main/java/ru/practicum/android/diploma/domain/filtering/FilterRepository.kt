package ru.practicum.android.diploma.domain.filtering

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState

interface FilterRepository {
    fun searchIndustries(): Flow<SearchState<List<FilterIndustryModel>>>
}
