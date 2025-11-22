package ru.practicum.android.diploma.data.network.request

sealed interface VacanciesRequest {
    data object Industries : VacanciesRequest
    data object Areas : VacanciesRequest
}
