package ru.practicum.android.diploma.data.network.response

import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.dto.VacancyDetailDto

data class VacancyDetailResponse(
    val result: VacancyDetailDto
) : Response()
