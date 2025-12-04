package ru.practicum.android.diploma.data.localstorage.dto

data class FilterDto(
    val areaId: Int? = null,
    val areaName: String? = null,
    val industryId: Int? = null,
    val industryName: String? = null,
    val salary: Int? = null,
    val onlyWithSalary: Boolean? = null
)
