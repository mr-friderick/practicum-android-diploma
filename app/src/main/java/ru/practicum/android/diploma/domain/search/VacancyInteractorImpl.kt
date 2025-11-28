package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancySearchState

class VacancyInteractorImpl(
    private val repository: VacancyRepository
) : VacancyInteractor {
    override fun searchVacancy(
        text: String,
        page: Int,
        filter: FilterModel?
    ): Flow<VacancySearchState> {
        return repository.searchVacancy(text, page, filter)
    }
}
