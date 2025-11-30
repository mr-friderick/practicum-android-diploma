package ru.practicum.android.diploma.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancySearchState

class VacancyInteractorImpl(
    private val repository: VacancyRepository
) : VacancyInteractor {
    override fun searchVacancy(
        text: String,
        filter: FilterModel?
    ): Flow<PagingData<VacancyDetailModel>> {
        return repository.searchVacancy(text, filter)
    }

    override fun searchVacancyDetail(id: String): Flow<VacancySearchState> {
        return repository.searchVacancyDetail(id)
    }
}
