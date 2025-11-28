package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancySearchState

interface VacancyInteractor {
    fun searchVacancy(
        text: String,
        page: Int,
        filter: FilterModel? = null
    ): Flow<VacancySearchState>

    fun searchVacancyDetail(
        id: String
    ): Flow<VacancySearchState>
}
