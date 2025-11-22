package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.network.request.VacanciesRequest

interface NetworkClient {
    suspend fun doRequest(request: VacanciesRequest): Response
}
