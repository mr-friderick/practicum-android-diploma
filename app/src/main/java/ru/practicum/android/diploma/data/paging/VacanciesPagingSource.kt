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
        // Всегда запрашиваем только PAGE_SIZE элементов, игнорируя params.loadSize
        val filters = baseFiltersProvider().copy(page = page, perPage = PAGE_SIZE)

        val response = networkClient.doRequest(filters)
        return when (response.resultCode) {
            HttpCode.OK -> {
                val data = (response as? VacancyResponse)?.result
                    ?: return LoadResult.Error(VacancyPagingException.Unknown("Пустое тело ответа"))

                if (data.items.isEmpty()) {
                    LoadResult.Error(VacancyPagingException.NotFound)
                } else {
                    // API возвращает правильное количество элементов (20 на страницу, кроме последней)
                    val items = data.items.map(mapper)
                    // Определяем последнюю страницу: 
                    // - если текущая страница >= последней страницы (pages - 1, т.к. нумерация с 0)
                    // - или если pages = 0 (нет страниц)
                    val isLastPage = data.pages == 0 || page >= (data.pages - 1)
                    // Если это последняя страница, nextKey должен быть null, чтобы предотвратить дальнейшие запросы
                    val nextKey = if (isLastPage) null else page + 1

                    LoadResult.Page(
                        data = items,
                        prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                        nextKey = nextKey
                    )
                }
            }

            HttpCode.BAD_REQUEST -> LoadResult.Error(
                VacancyPagingException.ServerError(
                    HttpCode.BAD_REQUEST,
                    "Неверный запрос: ${response.errorMassage}"
                )
            )
            HttpCode.NO_AUTH -> LoadResult.Error(
                VacancyPagingException.ServerError(
                    HttpCode.NO_AUTH,
                    "Ошибка авторизации: ${response.errorMassage}"
                )
            )
            HttpCode.NOT_FOUND -> LoadResult.Error(VacancyPagingException.NotFound)
            HttpCode.INTERNAL_ERROR -> LoadResult.Error(
                VacancyPagingException.ServerError(
                    HttpCode.INTERNAL_ERROR,
                    "Внутренняя ошибка сервера: ${response.errorMassage}"
                )
            )
            HttpCode.NOT_CONNECTION -> LoadResult.Error(VacancyPagingException.NoInternet)
            else -> LoadResult.Error(
                VacancyPagingException.ServerError(response.resultCode, response.errorMassage)
            )
        }
    }

    private companion object {
        const val FIRST_PAGE_INDEX = 0
        const val PAGE_SIZE = 20 // API возвращает 20 элементов на страницу
    }
}
