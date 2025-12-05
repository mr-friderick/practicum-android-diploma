package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.IndustriesResponse
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.filtering.FilterRepository
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.util.NetworkMonitor

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkMonitor: NetworkMonitor
) : FilterRepository {

    override fun searchIndustries(): Flow<VacancySearchState> = flow {
        if (!networkMonitor.isOnline()) {
            emit(VacancySearchState.NoInternet)
        } else {
            val response = networkClient.doRequest(
                VacanciesRequest.Industries
            )

            when (response.resultCode) {
                HttpCode.OK -> {
                    val foundContent = (response as IndustriesResponse).results
                    emit(
                        VacancySearchState.Industry(
                            foundContent.map { it.toModel() }
                        )
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
