package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancySearchState

interface VacancyRepository {
    fun searchVacancy(
        text: String,
        filter: FilterModel? = null
    ): Flow<PagingData<VacancyDetailModel>>

    fun searchVacancyDetail(
        id: String
    ): Flow<VacancySearchState>
}
