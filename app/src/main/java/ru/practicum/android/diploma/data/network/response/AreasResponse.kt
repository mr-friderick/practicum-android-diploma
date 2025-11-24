package ru.practicum.android.diploma.data.network.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.dto.FilterAreaDto

data class AreasResponse(
    val results: List<FilterAreaDto>
) : Response()
