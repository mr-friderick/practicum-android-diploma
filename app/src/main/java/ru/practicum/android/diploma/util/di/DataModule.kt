package ru.practicum.android.diploma.util.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.database.AppDatabase

val dataModule = module {
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "diploma-database"
        ).build()
    }

    single { get<AppDatabase>().favoriteVacancyDao() }
}
