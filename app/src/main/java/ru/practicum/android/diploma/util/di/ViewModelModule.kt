package ru.practicum.android.diploma.util.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favourites.viewmodel.FavoriteViewModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustrySelectViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.CountrySelectViewModel
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        VacancyDetailViewModel(get(), get())
    }

    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        IndustrySelectViewModel(get())
    }

    viewModel {
        CountrySelectViewModel(get())
    }

    viewModel {
        FilterViewModel(get())
    }
}
