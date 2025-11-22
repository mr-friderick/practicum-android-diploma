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
                    is VacanciesRequest.VacancyDetail -> {
                        VacancyDetailResponse(
                            result = vacanciesAPI.getVacancyDetail(request.id)
                        )
                    }
                    is VacanciesRequest.Vacancy -> {
                        val filters = mutableMapOf<String, Any>()
                        if (request.area != null) {
                            filters["area"] = request.area
                        }
                        if (request.industry != null) {
                            filters["industry"] = request.industry
                        }
                        if (!request.text.isNullOrEmpty()) {
                            filters["text"] = request.text
                        }
                        if (request.salary != null) {
                            filters["salary"] = request.salary
                        }
                        if (request.page != null) {
                            filters["page"] = request.page
                        }
                        if (request.onlyWithSalary != null) {
                            filters["only_with_salary"] = request.onlyWithSalary
                        }

                        VacancyResponse(
                            result = vacanciesAPI.getVacancy(filters)
                        )
                    }
                }
            } catch (e: HttpException) {
                Response().apply {
                    resultCode = e.code()
                }
            } catch (e: IOException) {
                Response().apply {
                    resultCode = HttpCode.NOT_CONNECTION
                }
            }
        }
        return result
    }
}
