package ru.practicum.android.diploma.presentation.vacancy.viewmodel

import ru.practicum.android.diploma.domain.models.VacancyDetailModel

interface VacancyDetailViewState {
    object Loading : VacancyDetailViewState
    data class VacancyDetail(
        val vacancyDetail: VacancyDetailModel,
        val isFavorite: Boolean
    ) : VacancyDetailViewState
    object NotFound : VacancyDetailViewState
    object NoInternet : VacancyDetailViewState
    data class Error(val message: String) : VacancyDetailViewState
}
