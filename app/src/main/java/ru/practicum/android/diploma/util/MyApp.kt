package ru.practicum.android.diploma.util

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.practicum.android.diploma.util.di.dataModule
import ru.practicum.android.diploma.util.di.interactorModule
import ru.practicum.android.diploma.util.di.repositoryModule
import ru.practicum.android.diploma.util.di.viewModelModule

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MyApp)
            modules(dataModule, interactorModule, repositoryModule, viewModelModule)
        }
    }
}
