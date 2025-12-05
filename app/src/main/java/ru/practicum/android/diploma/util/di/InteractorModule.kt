package ru.practicum.android.diploma.util.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.favourites.FavoriteVacancyInteractor
import ru.practicum.android.diploma.domain.favourites.FavoriteVacancyInteractorImpl
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.filtering.FilterInteractorImpl
import ru.practicum.android.diploma.domain.search.VacancyInteractor
import ru.practicum.android.diploma.domain.search.VacancyInteractorImpl

val interactorModule = module {
    factory<VacancyInteractor> {
        VacancyInteractorImpl(get())
    }

    factory<FavoriteVacancyInteractor> {
        FavoriteVacancyInteractorImpl(get())
    }

    factory<FilterInteractor> {
        FilterInteractorImpl(get())
    }
}
