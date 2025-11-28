package ru.practicum.android.diploma.util.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.VacancyRepositoryImpl
import ru.practicum.android.diploma.domain.search.VacancyRepository

val repositoryModule = module {
    factory<VacancyRepository> {
        VacancyRepositoryImpl(get(), get())
    }
}
