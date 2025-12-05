package ru.practicum.android.diploma.domain.models

fun FilterModel.hasAnyFilter(): Boolean {
    return areaId != null || industryId != null || salary != null || onlyWithSalary == true
}

fun FilterModel.isEmpty(): Boolean {
    return !hasAnyFilter()
}

fun FilterModel.isEqual(other: FilterModel?): Boolean {
    if (other == null) return isEmpty()
    return areaId == other.areaId &&
        areaName == other.areaName &&
        industryId == other.industryId &&
        industryName == other.industryName &&
        salary == other.salary &&
        onlyWithSalary == other.onlyWithSalary
}

