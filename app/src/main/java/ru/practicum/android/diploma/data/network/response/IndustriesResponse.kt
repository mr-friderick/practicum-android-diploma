package ru.practicum.android.diploma.data.network.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.dto.FilterIndustryDto

data class IndustriesResponse(
    val results: List<FilterIndustryDto>
) : Response()
