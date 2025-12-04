package ru.practicum.android.diploma.presentation.favourites.viewmodel

import ru.practicum.android.diploma.domain.models.VacancyDetailModel

interface FavoriteViewState {
    object Loading : FavoriteViewState
    object Empty : FavoriteViewState
    object Error : FavoriteViewState
    data class Content(val vacancies: List<VacancyDetailModel>) : FavoriteViewState
}
