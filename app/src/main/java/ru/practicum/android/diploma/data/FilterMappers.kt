package ru.practicum.android.diploma.data

import ru.practicum.android.diploma.data.localstorage.dto.FilterDto
import ru.practicum.android.diploma.domain.models.FilterModel

fun FilterDto.toModel(): FilterModel {
    return FilterModel(
        areaId = areaId,
        areaName = areaName,
        industryId = industryId,
        industryName = industryName,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}

fun FilterModel.toDto(): FilterDto {
    return FilterDto(
        areaId = areaId,
        areaName = areaName,
        industryId = industryId,
        industryName = industryName,
        salary = salary,
        onlyWithSalary = onlyWithSalary
    )
}

