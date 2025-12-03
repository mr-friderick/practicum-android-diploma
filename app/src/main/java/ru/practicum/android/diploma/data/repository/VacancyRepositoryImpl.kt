package ru.practicum.android.diploma.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.VacancyDetailResponse
import ru.practicum.android.diploma.data.paging.VacanciesPagingSource
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.models.VacancySearchState
import ru.practicum.android.diploma.domain.search.VacancyRepository
import ru.practicum.android.diploma.util.NetworkMonitor

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkMonitor: NetworkMonitor
) : VacancyRepository {

    override fun searchVacancy(
        text: String,
        filter: FilterModel?,
        onTotalCount: (Int?) -> Unit
    ): Flow<PagingData<VacancyDetailModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = 1, // Загружаем следующую страницу когда остался 1 элемент до конца
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                VacanciesPagingSource(
                    networkClient = networkClient,
                    baseFiltersProvider = {
                        VacanciesRequest.Vacancy(
                            text = text,
                            page = FIRST_PAGE_INDEX,
                            area = filter?.areaId,
                            industry = filter?.industryId,
                            salary = filter?.salary,
                            onlyWithSalary = filter?.onlyWithSalary,
                            perPage = PAGE_SIZE
                        )
                    },
                    mapper = { it.toModel() },
                    networkMonitor = networkMonitor,
                    onTotalCount = onTotalCount
                )
            }
        ).flow
    }

    override fun searchVacancyDetail(id: String): Flow<VacancySearchState> = flow {
        if (!networkMonitor.isOnline()) {
            emit(VacancySearchState.NoInternet)
        } else {
            val response = networkClient.doRequest(
                VacanciesRequest.VacancyDetail(id)
            )

            when (response.resultCode) {
                HttpCode.OK -> {
                    val foundContent = (response as VacancyDetailResponse).result
                    emit(
                        VacancySearchState.VacancyDetail(foundContent.toModel())
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

    private companion object {
        const val PAGE_SIZE = 20 // API возвращает 20 элементов на страницу
        const val FIRST_PAGE_INDEX = 0
    }
}
