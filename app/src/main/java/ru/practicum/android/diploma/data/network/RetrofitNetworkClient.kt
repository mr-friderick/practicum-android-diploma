package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.AreasResponse
import ru.practicum.android.diploma.data.network.response.IndustriesResponse
import ru.practicum.android.diploma.data.network.response.VacancyDetailResponse
import ru.practicum.android.diploma.data.network.response.VacancyResponse
import java.io.IOException

class RetrofitNetworkClient(
    private val vacanciesAPI: VacanciesAPI
) : NetworkClient {

    override suspend fun doRequest(request: VacanciesRequest): Response {
        val result = withContext(Dispatchers.IO) {
            try {
                requestProcessing(request)
            } catch (e: HttpException) {
                Response().apply {
                    resultCode = e.code()
                    errorMassage = e.message()
                }
            } catch (e: IOException) {
                Response().apply {
                    resultCode = HttpCode.NOT_CONNECTION
                    errorMassage = e.message ?: ""
                }
            }
        }
        return result
    }

    private suspend fun requestProcessing(request: VacanciesRequest): Response =
        when (request) {
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

            is VacanciesRequest.VacancyDetail -> {
                VacancyDetailResponse(
                    result = vacanciesAPI.getVacancyDetail(request.id)
                )
            }

            is VacanciesRequest.Vacancy -> {
                val filters = filterForVacancyRequest(request)
                VacancyResponse(
                    result = vacanciesAPI.getVacancy(filters)
                )
            }
        }

    private fun filterForVacancyRequest(
        vacancyRequest: VacanciesRequest.Vacancy
    ): Map<String, Any> {
        val filters = mutableMapOf<String, Any>()
        if (vacancyRequest.area != null) {
            filters["area"] = vacancyRequest.area
        }
        if (vacancyRequest.industry != null) {
            filters["industry"] = vacancyRequest.industry
        }
        if (!vacancyRequest.text.isNullOrEmpty()) {
            filters["text"] = vacancyRequest.text
        }
        if (vacancyRequest.salary != null) {
            filters["salary"] = vacancyRequest.salary
        }
        if (vacancyRequest.page != null) {
            filters["page"] = vacancyRequest.page
        }
        if (vacancyRequest.onlyWithSalary != null) {
            filters["only_with_salary"] = vacancyRequest.onlyWithSalary
        }

        return filters
    }
}
