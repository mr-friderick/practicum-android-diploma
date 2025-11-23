package ru.practicum.android.diploma.util.di

import android.content.Context
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.localstorage.LocalStorage
import ru.practicum.android.diploma.data.localstorage.SharedPrefLocalStorage
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient
import ru.practicum.android.diploma.data.network.VacanciesAPI

private const val VACANCY_BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
private const val FILE_PREFERENCES = "local_preferences"

val dataModule = module {

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<VacanciesAPI> {
        Retrofit.Builder()
            .baseUrl(VACANCY_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VacanciesAPI::class.java)
    }

    single<LocalStorage> {
        SharedPrefLocalStorage(get(), get())
    }

    single {
        androidContext().getSharedPreferences(FILE_PREFERENCES, Context.MODE_PRIVATE)
    }

    single {
        Gson()
    }
}
