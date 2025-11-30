package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel

interface VacancyInteractor {
    fun searchVacancy(
        text: String,
        filter: FilterModel? = null,
        onTotalCount: (Int?) -> Unit = {}
    ): Flow<PagingData<VacancyDetailModel>>
}
