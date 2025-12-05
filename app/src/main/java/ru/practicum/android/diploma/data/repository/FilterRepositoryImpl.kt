package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.localstorage.LocalStorage
import ru.practicum.android.diploma.data.toDto
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.filtering.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterModel

class FilterRepositoryImpl(
    private val localStorage: LocalStorage
) : FilterRepository {

    override suspend fun getFilter(): FilterModel? = withContext(Dispatchers.IO) {
        val filterDto = localStorage.read()
        // Проверяем, что хотя бы одно поле не null и не пустое
        if (filterDto.areaId != null || filterDto.industryId != null ||
            filterDto.salary != null || filterDto.onlyWithSalary == true
        ) {
            filterDto.toModel()
        } else {
            null
        }
    }

    override suspend fun saveFilter(filter: FilterModel) = withContext(Dispatchers.IO) {
        localStorage.save(filter.toDto())
    }

    override suspend fun clearFilter() = withContext(Dispatchers.IO) {
        localStorage.clear()
    }
}

