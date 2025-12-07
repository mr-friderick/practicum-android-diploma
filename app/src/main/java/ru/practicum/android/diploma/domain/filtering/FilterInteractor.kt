package ru.practicum.android.diploma.domain.filtering

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState

interface FilterInteractor {
    fun searchIndustries(): Flow<SearchState<List<FilterIndustryModel>>>

    fun searchCountries(): Flow<SearchState<List<FilterAreaModel>>>

    fun searchRegions(): Flow<SearchState<List<FilterAreaModel>>>
}
