package ru.practicum.android.diploma.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.VacancyResponse
import ru.practicum.android.diploma.domain.exception.VacancyPagingException
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.util.NetworkMonitor

class VacanciesPagingSource(
    private val networkClient: NetworkClient,
    private val baseFiltersProvider: () -> VacanciesRequest.Vacancy,
    private val mapper: (VacancyDetailDto) -> VacancyDetailModel,
    private val networkMonitor: NetworkMonitor
) : PagingSource<Int, VacancyDetailModel>() {

    override fun getRefreshKey(state: PagingState<Int, VacancyDetailModel>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition)
        return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VacancyDetailModel> {
        if (!networkMonitor.isOnline()) {
            return LoadResult.Error(VacancyPagingException.NoInternet)
        }

        val page = params.key ?: FIRST_PAGE_INDEX
        val filters = baseFiltersProvider().copy(page = page, perPage = PAGE_SIZE)

        val response = networkClient.doRequest(filters)
        return when (response.resultCode) {
            HttpCode.OK -> {
                val data = (response as? VacancyResponse)?.result
                    ?: return LoadResult.Error(VacancyPagingException.Unknown("Пустое тело ответа"))

                if (data.items.isEmpty()) {
                    LoadResult.Error(VacancyPagingException.NotFound)
                } else {
                    val items = data.items.map(mapper)
                    val isLastPage = page >= data.pages - 1

                    LoadResult.Page(
                        data = items,
                        prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                        nextKey = if (isLastPage) null else page + 1
                    )
                }
            }

            HttpCode.NOT_FOUND -> LoadResult.Error(VacancyPagingException.NotFound)
            HttpCode.NOT_CONNECTION -> LoadResult.Error(VacancyPagingException.NoInternet)
            else -> LoadResult.Error(
                VacancyPagingException.ServerError(response.resultCode, response.errorMassage)
            )
        }
    }

    private companion object {
        const val FIRST_PAGE_INDEX = 0
        const val PAGE_SIZE = 4
    }
}
