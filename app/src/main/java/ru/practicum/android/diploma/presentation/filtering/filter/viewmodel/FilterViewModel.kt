package ru.practicum.android.diploma.presentation.filtering.filter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.hasAnyFilter
import ru.practicum.android.diploma.domain.models.isEqual

class FilterViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _currentFilter = MutableStateFlow<FilterModel?>(null)
    val currentFilter: StateFlow<FilterModel?> = _currentFilter.asStateFlow()

    private val _savedFilter = MutableStateFlow<FilterModel?>(null)

    val showResetButton: StateFlow<Boolean> = combine(
        _currentFilter
    ) { values ->
        val filter = values[0]
        filter?.hasAnyFilter() == true
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    val showApplyButton: StateFlow<Boolean> = combine(
        _currentFilter,
        _savedFilter
    ) { values ->
        val current = values[0]
        val saved = values[1]
        current?.isEqual(saved) != true
    }.stateIn(
        scope = viewModelScope,
        started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        loadFilter()
    }

    fun loadFilter() {
        viewModelScope.launch {
            val savedFilter = filterInteractor.getFilter()
            _currentFilter.value = savedFilter ?: FilterModel()
            _savedFilter.value = savedFilter
        }
    }

    fun updateArea(areaId: Int?, areaName: String?) {
        val updatedFilter = _currentFilter.value?.copy(
            areaId = areaId,
            areaName = areaName
        ) ?: FilterModel(areaId = areaId, areaName = areaName)
        updateFilter(updatedFilter)
    }

    fun updateIndustry(industryId: Int?, industryName: String?) {
        val updatedFilter = _currentFilter.value?.copy(
            industryId = industryId,
            industryName = industryName
        ) ?: FilterModel(industryId = industryId, industryName = industryName)
        updateFilter(updatedFilter)
    }

    fun updateSalary(salary: Int?) {
        val updatedFilter = _currentFilter.value?.copy(salary = salary)
            ?: FilterModel(salary = salary)
        updateFilter(updatedFilter)
    }

    fun updateOnlyWithSalary(onlyWithSalary: Boolean?) {
        val updatedFilter = _currentFilter.value?.copy(onlyWithSalary = onlyWithSalary)
            ?: FilterModel(onlyWithSalary = onlyWithSalary)
        updateFilter(updatedFilter)
    }

    fun resetFilter() {
        val emptyFilter = FilterModel()
        updateFilter(emptyFilter)
    }

    fun applyFilter() {
        viewModelScope.launch {
            val filter = _currentFilter.value
            if (filter != null) {
                filterInteractor.saveFilter(filter)
                _savedFilter.value = filter
            }
        }
    }

    fun onBackClick() {
        // Автосохранение уже работает при каждом изменении через updateFilter
        // Но при нажатии назад тоже сохраняем для надежности
        viewModelScope.launch {
            val filter = _currentFilter.value
            if (filter != null) {
                filterInteractor.saveFilter(filter)
                _savedFilter.value = filter
            }
        }
    }

    private fun updateFilter(filter: FilterModel) {
        _currentFilter.value = filter
        // Автосохранение согласно ТЗ
        // НЕ обновляем _savedFilter здесь, чтобы кнопка "Применить" корректно работала
        viewModelScope.launch {
            filterInteractor.saveFilter(filter)
        }
    }
}
