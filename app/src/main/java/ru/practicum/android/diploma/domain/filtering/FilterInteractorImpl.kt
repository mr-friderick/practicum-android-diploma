package ru.practicum.android.diploma.domain.filtering

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancySearchState

class FilterInteractorImpl(
    private val repository: FilterRepository
) : FilterInteractor {

    override fun searchIndustries(): Flow<VacancySearchState> {
        return repository.searchIndustries()
    }
}
