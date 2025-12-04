package ru.practicum.android.diploma.domain.exception

sealed class VacancyPagingException(message: String) : Exception(message) {
    object NoInternet : VacancyPagingException("Отсутствует подключение к интернету")
    object NotFound : VacancyPagingException("Ничего не найдено по запросу")
    data class ServerError(val code: Int, val detail: String) :
        VacancyPagingException("Ошибка сервера: $code $detail")

    data class Unknown(val detail: String) :
        VacancyPagingException("Непредвиденная ошибка: $detail")
}

