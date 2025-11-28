package ru.practicum.android.diploma.presentation.search.viewmodel

import ru.practicum.android.diploma.domain.models.VacancyModel

sealed interface SearchViewState {
    object Default : SearchViewState
    object Loading : SearchViewState
    data class Vacancy(val vacancy: VacancyModel) : SearchViewState
    object NotFound : SearchViewState
    object NoInternet : SearchViewState
    data class Error(val message: String) : SearchViewState
}
