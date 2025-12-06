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
        // Валидация: только цифры (целые числа), запрет на лидирующие нули
        val validatedSalary = buildString {
            var hasNonZero = false
            for (char in salary) {
                if (char.isDigit()) {
                    // Пропускаем лидирующие нули
                    if (char != '0' || hasNonZero || isEmpty()) {
                        append(char)
                        if (char != '0') hasNonZero = true
                    }
                }
            }
            // Если ничего не добавили, но был ввод "0" - оставляем "0"
            if (isEmpty() && salary.contains('0')) {
                append('0')
            }
        }.take(9) // Максимум 9 цифр

        val salaryValue = validatedSalary.toIntOrNull()

        val currentState = _filterState.value ?: FilterModel()
        _filterState.value = currentState.copy(
            salary = salaryValue,
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
