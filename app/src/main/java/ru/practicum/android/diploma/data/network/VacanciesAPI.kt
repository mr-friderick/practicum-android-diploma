package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.dto.FilterAreaDto
import ru.practicum.android.diploma.data.network.dto.FilterIndustryDto

interface VacanciesAPI {

    @GET("/areas")
    suspend fun getAreas(
        @Header("Authorization") token: String = TOKEN,
        @Header("Content-Type") contentType: String = CONTENT_TYPE
    ): List<FilterAreaDto>

    @GET("/industries")
    suspend fun getIndustries(
        @Header("Authorization") token: String = TOKEN,
        @Header("Content-Type") contentType: String = CONTENT_TYPE
    ): List<FilterIndustryDto>

    companion object {
        private const val CONTENT_TYPE = "application/json"
        private const val TOKEN = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    }
}
