package ru.practicum.android.diploma.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.network.request.VacanciesRequest
import ru.practicum.android.diploma.data.network.response.VacancyResponse
import ru.practicum.android.diploma.util.NetworkMonitor

class VacanciesPagingSource(
    private val networkClient: NetworkClient,
    private val baseFiltersProvider: () -> VacanciesRequest.Vacancy,
    private val mapper: (VacancyDetailDto) -> VacancyDetailDto = { it }, // заменить на доменную модель
    private val networkMonitor: NetworkMonitor
) : PagingSource<Int, VacancyDetailDto>() {

    override fun getRefreshKey(state: PagingState<Int, VacancyDetailDto>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val closestPage = state.closestPageToPosition(anchorPosition)
        return closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VacancyDetailDto> {
        if (!networkMonitor.isOnline()) {
            return LoadResult.Error(IllegalStateException("No internet connection"))
        }

        val page = params.key ?: FIRST_PAGE_INDEX
        val filters = baseFiltersProvider().copy(page = page)

        val response = networkClient.doRequest(filters)
        if (response.resultCode != HttpCode.OK) {
            return LoadResult.Error(
                IllegalStateException("Network error: ${response.resultCode} ${response.errorMassage}")
            )
        }

        val data = (response as? VacancyResponse)?.result
            ?: return LoadResult.Error(IllegalStateException("Unexpected response type"))

        val items = data.items.map(mapper)
        val isLastPage = page >= data.pages - 1

        return LoadResult.Page(
            data = items,
            prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
            nextKey = if (isLastPage) null else page + 1
        )
    }

    private companion object {
        const val FIRST_PAGE_INDEX = 0
    }
}
