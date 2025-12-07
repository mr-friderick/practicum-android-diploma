package ru.practicum.android.diploma.domain.filtering

import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.localstorage.LocalStorage
import ru.practicum.android.diploma.data.localstorage.dto.FilterDto
import ru.practicum.android.diploma.data.toDto
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.SearchState

class FilterInteractorImpl(
    private val repository: FilterRepository,
    private val localStorage: LocalStorage
) : FilterInteractor {

    override fun searchIndustries(): Flow<SearchState<List<FilterIndustryModel>>> {
        return repository.searchIndustries()
    }

    override fun searchCountries(): Flow<SearchState<List<FilterAreaModel>>> {
        return repository.searchCountries()
    }

    override fun saveFilter(filter: FilterModel) {
        localStorage.save(filter.toDto())
    }

    override fun getFilter(): FilterModel? {
        return try {
            val filterDto = localStorage.read()
            if (isFilterEmpty(filterDto)) {
                null
            } else {
                filterDto.toModel()
            }
        } catch (e: JsonSyntaxException) {
            null
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun isFilterEmpty(filterDto: FilterDto): Boolean {
        return filterDto.areaId == null &&
                filterDto.areaName == null &&
                filterDto.industryId == null &&
                filterDto.industryName == null &&
                filterDto.salary == null &&
                filterDto.onlyWithSalary == null
    }

    override fun clearFilter() {
        localStorage.clear()
    }
}
