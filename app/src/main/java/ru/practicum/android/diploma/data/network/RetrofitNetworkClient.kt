package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.AreasResponse
import ru.practicum.android.diploma.data.network.response.IndustriesResponse

class RetrofitNetworkClient(
    private val vacanciesAPI: VacanciesAPI
) : NetworkClient {

    override suspend fun doRequest(request: VacanciesRequest): Response {
        val result = withContext(Dispatchers.IO) {
            kotlin.runCatching {
                when(request) {
                    is VacanciesRequest.Industries -> {
                        IndustriesResponse(
                            results = vacanciesAPI.getIndustries()
                        )
                    }
                    is VacanciesRequest.Areas -> {
                        AreasResponse(
                            results = vacanciesAPI.getAreas()
                        )
                    }
                    else -> {
                        Response().apply {
                            resultCode = HttpCode.BAD_REQUEST
                        }
                    }
                }
            }.getOrDefault(
                Response().apply {
                    resultCode = HttpCode.NOT_CONNECTION
                }
            )
        }
        return result
    }
}
