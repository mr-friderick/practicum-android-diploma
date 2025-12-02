package ru.practicum.android.diploma.util.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel

val viewModelModule = module {
    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        VacancyDetailViewModel(
            get(),
            get()
        )
    }
}
