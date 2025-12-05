package ru.practicum.android.diploma.domain.filtering

import ru.practicum.android.diploma.domain.models.FilterModel

class FilterInteractorImpl(
    private val filterRepository: FilterRepository
) : FilterInteractor {

    override suspend fun getFilter(): FilterModel? {
        return filterRepository.getFilter()
    }

    override suspend fun saveFilter(filter: FilterModel) {
        filterRepository.saveFilter(filter)
    }

    override suspend fun clearFilter() {
        filterRepository.clearFilter()
    }
}

