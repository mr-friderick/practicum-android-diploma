package ru.practicum.android.diploma.presentation.filtering.filter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.FilterModel

class FilterViewModel : ViewModel() {

    private val _filterState = MutableLiveData(FilterModel())
    val filterState: LiveData<FilterModel> = _filterState

    // Для вычисления видимости кнопки "Сбросить"
    val showResetButton: LiveData<Boolean> = _filterState.map { state ->
        state.hasAnyFilter()
    }

    // Метод для проверки наличия фильтров (расширение для FilterModel)
    private fun FilterModel.hasAnyFilter(): Boolean {
        return areaId != null ||
            industryId != null ||
            salary != null ||
            onlyWithSalary == true
    }

    fun updateSalary(salary: String) {
        // Валидация: только цифры, ограничение длины
        val validatedSalary = salary
            .filter { it.isDigit() }
            .take(9)

        val salaryValue = validatedSalary.toIntOrNull()

        val currentState = _filterState.value ?: FilterModel()
        _filterState.value = currentState.copy(
            salary = salaryValue,
            // Автоматически включаем onlyWithSalary если ввели зарплату
            onlyWithSalary = salaryValue != null || currentState.onlyWithSalary == true
        )
    }

    fun updateHideWithoutSalary(hide: Boolean) {
        val currentState = _filterState.value ?: FilterModel()
        _filterState.value = currentState.copy(onlyWithSalary = hide)
    }

    fun updateWorkPlace(area: FilterAreaModel?) {
        val currentState = _filterState.value ?: FilterModel()
        _filterState.value = currentState.copy(
            areaId = area?.id,
            areaName = area?.name
        )
    }

    fun updateIndustry(industry: FilterIndustryModel?) {
        val currentState = _filterState.value ?: FilterModel()
        _filterState.value = currentState.copy(
            industryId = industry?.id,
            industryName = industry?.name
        )
    }

    fun resetFilters() {
        _filterState.value = FilterModel()
    }

    // Получить текст для отображения места работы
    fun getWorkPlaceText(): String {
        return _filterState.value?.areaName ?: "Не выбрано"
    }

    // Получить текст для отображения отрасли
    fun getIndustryText(): String {
        return _filterState.value?.industryName ?: "Не выбрано"
    }
}
