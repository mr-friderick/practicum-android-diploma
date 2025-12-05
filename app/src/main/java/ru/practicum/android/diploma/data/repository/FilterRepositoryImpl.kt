package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.IndustriesResponse
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.filtering.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState
import ru.practicum.android.diploma.util.NetworkMonitor

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkMonitor: NetworkMonitor
) : FilterRepository {

    override fun searchIndustries(): Flow<SearchState<List<FilterIndustryModel>>> = flow {
        if (!networkMonitor.isOnline()) {
            emit(SearchState.NoInternet)
        } else {
            val response = networkClient.doRequest(
                VacanciesRequest.Industries
            )

            when (response.resultCode) {
                HttpCode.OK -> {
                    val foundContent = (response as IndustriesResponse).results
                    emit(
                        SearchState.Success(
                            foundContent.map { it.toModel() }
                        )
                    )
                }

                else -> {
                    emit(SearchState.Error(response.errorMassage))
                }
            }
        }
    }
}
