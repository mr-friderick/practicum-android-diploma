package ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel

import ru.practicum.android.diploma.domain.models.FilterAreaModel

interface RegionViewState {
    object Loading : RegionViewState
    data class Region(val regions: List<FilterAreaModel>) : RegionViewState
    object NoInternet : RegionViewState
    data class Error(val message: String) : RegionViewState
}
