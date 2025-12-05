package ru.practicum.android.diploma.domain.filtering

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState

class FilterInteractorImpl(
    private val repository: FilterRepository
) : FilterInteractor {

    override fun searchIndustries(): Flow<SearchState<List<FilterIndustryModel>>> {
        return repository.searchIndustries()
    }
}
