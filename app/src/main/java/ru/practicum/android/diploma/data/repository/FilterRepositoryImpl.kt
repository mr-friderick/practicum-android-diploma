package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.Response
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.AreasResponse
import ru.practicum.android.diploma.data.network.response.IndustriesResponse
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.filtering.FilterRepository
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.domain.models.SearchState
import ru.practicum.android.diploma.util.NetworkMonitor

class FilterRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkMonitor: NetworkMonitor
) : FilterRepository {

    override fun searchIndustries(): Flow<SearchState<List<FilterIndustryModel>>> {
        return searchContent(VacanciesRequest.Industries) { response ->
            (response as IndustriesResponse).results
                .map { it.toModel() }
                .sortedBy { it.name }
        }
    }

    override fun searchCountries(): Flow<SearchState<List<FilterAreaModel>>> {
        return searchContent(VacanciesRequest.Areas) { response ->
            (response as AreasResponse).results.map { it.toModel() }
        }
    }

    override fun searchRegions(): Flow<SearchState<List<FilterAreaModel>>> {
        return searchContent(VacanciesRequest.Areas) { response ->
            (response as AreasResponse).results
                .map { it.toModel() }
                .let {
                    findRegions(it)
                }
                .sortedBy { it.name }
        }
    }

    private fun <T> searchContent(
        request: VacanciesRequest,
        transform: (Response) -> List<T>
    ): Flow<SearchState<List<T>>> = flow {
        if (!networkMonitor.isOnline()) {
            emit(SearchState.NoInternet)
        } else {
            val response = networkClient.doRequest(request)

            when (response.resultCode) {
                HttpCode.OK -> {
                    val foundContent = transform(response)
                    emit(SearchState.Success(foundContent))
                }

                else -> {
                    emit(SearchState.Error(response.errorMassage))
                }
            }
        }
    }

    private fun findRegions(areas: List<FilterAreaModel>): List<FilterAreaModel> {
        val result = mutableListOf<FilterAreaModel>()

        fun analysis(areas: List<FilterAreaModel>) {
            for (area in areas) {
                if (area.parentId != null) {
                    result.add(area)
                }
                if (area.areas.isNotEmpty()) {
                    analysis(area.areas)
                }
            }
        }

        analysis(areas)
        return result
    }
}
