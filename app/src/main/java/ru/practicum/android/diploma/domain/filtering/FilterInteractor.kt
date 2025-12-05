package ru.practicum.android.diploma.domain.filtering

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancySearchState

interface FilterInteractor {
    fun searchIndustries(): Flow<VacancySearchState>
}
