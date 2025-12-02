package ru.practicum.android.diploma.presentation.favourites.viewmodel

import ru.practicum.android.diploma.domain.models.VacancyDetailModel

interface FavoriteViewState {
    object Empty : FavoriteViewState
    data class Content (val vacancies: List<VacancyDetailModel>) : FavoriteViewState
}
