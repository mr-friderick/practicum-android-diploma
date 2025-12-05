package ru.practicum.android.diploma.util.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.FavoriteVacancyRepositoryImpl
import ru.practicum.android.diploma.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.favourites.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.filtering.FilterRepository
import ru.practicum.android.diploma.domain.search.VacancyRepository

val repositoryModule = module {
    factory<VacancyRepository> {
        VacancyRepositoryImpl(get(), get())
    }

    factory<FavoriteVacancyRepository> {
        FavoriteVacancyRepositoryImpl(get(), get())
    }

    factory<FilterRepository> {
        FilterRepositoryImpl(get(), get())
    }
}
