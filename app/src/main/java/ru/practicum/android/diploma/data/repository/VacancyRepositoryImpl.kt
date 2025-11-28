package ru.practicum.android.diploma.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.paging.VacanciesPagingSource
import ru.practicum.android.diploma.data.toModel
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.domain.search.VacancyRepository
import ru.practicum.android.diploma.util.NetworkMonitor

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient,
    private val networkMonitor: NetworkMonitor
) : VacancyRepository {

    override fun searchVacancy(
        text: String,
        filter: FilterModel?
    ): Flow<PagingData<VacancyDetailModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = 1, // Минимальное значение для работы Paging, загружаем когда остался 1 элемент
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
                    networkMonitor = networkMonitor
                )
            }
        ).flow
    }

    private companion object {
        const val PAGE_SIZE = 4
        const val FIRST_PAGE_INDEX = 0
    }
}
