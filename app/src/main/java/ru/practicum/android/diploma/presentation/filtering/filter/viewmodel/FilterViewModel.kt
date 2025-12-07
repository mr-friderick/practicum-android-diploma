package ru.practicum.android.diploma.presentation.filtering.filter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterModel

class FilterViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _filterState = MutableStateFlow<FilterModel?>(null)
    val filterState: StateFlow<FilterModel?> = _filterState.asStateFlow()

    private val _filterApplied = MutableStateFlow(false)
    val filterApplied: StateFlow<Boolean> = _filterApplied.asStateFlow()

    init {
        loadFilter()
    }

    fun loadFilter() {
        viewModelScope.launch {
            _filterState.value = filterInteractor.getFilter() ?: FilterModel()
        }
    }

    fun updateArea(areaId: Int?, areaName: String?) {
        _filterState.value = _filterState.value?.copy(
            areaId = areaId,
            areaName = areaName
        ) ?: FilterModel(areaId = areaId, areaName = areaName)
    }

    fun updateCountry(countryId: Int, countryName: String) {
        _filterState.value = _filterState.value?.copy(
            areaId = countryId,
            areaName = countryName
        ) ?: FilterModel(areaId = countryId, areaName = countryName)
    }

    fun updateRegion(regionId: Int, regionName: String, countryId: Int?, countryName: String?) {
        val displayName = if (countryName != null) {
            "$countryName, $regionName"  // "Россия, Москва"
        } else {
            regionName  // Если страну не нашли
        }

        _filterState.value = _filterState.value?.copy(
            areaId = regionId,      // Сохраняем ID региона
            areaName = displayName  // Сохраняем "Страна, Регион"
        ) ?: FilterModel(areaId = regionId, areaName = displayName)
    }

    fun updateIndustry(industryId: Int?, industryName: String?) {
        _filterState.value = _filterState.value?.copy(
            industryId = industryId,
            industryName = industryName
        ) ?: FilterModel(industryId = industryId, industryName = industryName)
    }

    fun updateSalary(salary: Int?) {
        _filterState.value = _filterState.value?.copy(
            salary = salary
        ) ?: FilterModel(salary = salary)
    }

    fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        _filterState.value = _filterState.value?.copy(
            onlyWithSalary = onlyWithSalary
        ) ?: FilterModel(onlyWithSalary = onlyWithSalary)
    }

    fun applyFilter(): FilterModel? {
        val currentFilter = _filterState.value
        if (currentFilter != null) {
            filterInteractor.saveFilter(currentFilter)
            _filterApplied.value = true
        }
        return currentFilter
    }

    fun resetFilter() {
        _filterState.value = FilterModel()
        filterInteractor.clearFilter()
        _filterApplied.value = true
    }

    fun clearFilterAppliedFlag() {
        _filterApplied.value = false
    }

    fun onBackClick() {
        // Возврат без сохранения - изменения остаются в filterState, но не применяются к поиску
    }

    override fun onCleared() {
        super.onCleared()
    }
}
