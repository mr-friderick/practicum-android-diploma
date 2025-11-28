package ru.practicum.android.diploma.util.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.database.AppDatabase
import ru.practicum.android.diploma.data.localstorage.LocalStorage
import ru.practicum.android.diploma.data.localstorage.SharedPrefLocalStorage
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.VacanciesAPI
import ru.practicum.android.diploma.util.Constants
import ru.practicum.android.diploma.util.NetworkMonitor

val dataModule = module {

    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "diploma-database"
        ).build()
    }

    single { get<AppDatabase>().favoriteVacancyDao() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<VacanciesAPI> {
        Retrofit.Builder()
            .baseUrl(Constants.VACANCY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VacanciesAPI::class.java)
    }

    single<LocalStorage> {
        SharedPrefLocalStorage(get(), get())
    }

    single {
        androidContext().getSharedPreferences(Constants.FILE_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Gson()
    }

    single {
        NetworkMonitor(androidContext())
    }
}
