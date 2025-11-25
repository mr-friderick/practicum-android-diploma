package ru.practicum.android.diploma.data.network.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.dto.VacancyDto

data class VacancyResponse(
    val result: VacancyDto
) : Response()
