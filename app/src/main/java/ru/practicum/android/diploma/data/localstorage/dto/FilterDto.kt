package ru.practicum.android.diploma.data.localstorage.dto

data class FilterDto(
    val areaId: Int?,
    val areaName: String?,
    val industryId: Int?,
    val industryName: String?,
    val text: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean?
)
