package ru.practicum.android.diploma.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.practicum.android.diploma.data.network.HttpCode
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.Response
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
    private val networkMonitor: NetworkMonitor,
    private val onTotalCount: (Int?) -> Unit = {}
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

        return handleResponse(response, page)
    }

    private fun handleResponse(
        response: Response,
        page: Int
    ): LoadResult<Int, VacancyDetailModel> {
        return when (response.resultCode) {
            HttpCode.OK -> handleSuccessResponse(response, page)
            HttpCode.BAD_REQUEST -> {
                createServerError(HttpCode.BAD_REQUEST, response, "Неверный запрос")
            }
            HttpCode.NO_AUTH -> {
                createServerError(HttpCode.NO_AUTH, response, "Ошибка авторизации")
            }
            HttpCode.NOT_FOUND -> {
                // Если сервер вернул NotFound — явно указываем 0 найденных
                onTotalCount(0)
                LoadResult.Error(VacancyPagingException.NotFound)
            }
            HttpCode.INTERNAL_ERROR -> {
                createServerError(
                    HttpCode.INTERNAL_ERROR,
                    response,
                    "Внутренняя ошибка сервера"
                )
            }
            HttpCode.NOT_CONNECTION -> {
                LoadResult.Error(VacancyPagingException.NoInternet)
            }
            else -> {
                LoadResult.Error(
                    VacancyPagingException.ServerError(response.resultCode, response.errorMassage)
                )
            }
        }
    }

    private fun handleSuccessResponse(
        response: Response,
        page: Int
    ): LoadResult<Int, VacancyDetailModel> {
        val data = (response as? VacancyResponse)?.result
            ?: run {
                return LoadResult.Error(VacancyPagingException.Unknown("Пустое тело ответа"))
            }
        // ОТЛАДКА - добавьте эту строку
        println("DEBUG PagingSource: data.found = ${data.found}, page = $page")
        onTotalCount(data.found)

        return if (data.items.isEmpty()) {
            // Если items пустые, но found > 0 - это странно, но следуем данным API
            if (data.found == 0) {
                LoadResult.Error(VacancyPagingException.NotFound)
            } else {
                // Если API говорит, что есть результаты, но items пуст - это ошибка данных
                LoadResult.Error(VacancyPagingException.Unknown("Нет элементов при found = ${data.found}"))
            }
        }  else {
            val items = data.items.map(mapper)
            val isLastPage = data.pages == 0 || page >= data.pages - 1
            val nextKey = if (isLastPage) null else page + 1

            LoadResult.Page(
                data = items,
                prevKey = if (page == FIRST_PAGE_INDEX) null else page - 1,
                nextKey = nextKey
            )
        }
    }

    private fun createServerError(
        code: Int,
        response: Response,
        message: String
    ): LoadResult.Error<Int, VacancyDetailModel> {
        return LoadResult.Error(
            VacancyPagingException.ServerError(
                code,
                "$message: ${response.errorMassage}"
            )
        )
    }

    private companion object {
        const val FIRST_PAGE_INDEX = 0
        const val PAGE_SIZE = 20 // API возвращает 20 элементов на страницу
    }
}
