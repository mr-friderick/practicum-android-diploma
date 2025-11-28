package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.VacancyResponse
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyRepository
import ru.practicum.android.diploma.util.NetworkMonitor

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkMonitor: NetworkMonitor
) : VacancyRepository {

    override fun searchVacancy(
        text: String,
        page: Int,
        filter: FilterModel?
    ): Flow<VacancySearchState> = flow {
        if (!networkMonitor.isOnline()) {
            emit(VacancySearchState.NoInternet)
        } else {
            val response = networkClient.doRequest(
                VacanciesRequest.Vacancy(
                    text = text,
                    page = page,
                    area = filter?.areaId,
                    industry = filter?.industryId,
                    salary = filter?.salary,
                    onlyWithSalary = filter?.onlyWithSalary
                )
            )
            when (response.resultCode) {
                HttpCode.OK -> {
                    val foundContent = (response as VacancyResponse).result
                    emit(
                        VacancySearchState.Content(foundContent.toModel())
                    )
                }

                HttpCode.NOT_FOUND -> {
                    emit(VacancySearchState.NotFound)
                }

                else -> {
                    emit(VacancySearchState.Error(response.errorMassage))
                }
            }
        }
    }
}
