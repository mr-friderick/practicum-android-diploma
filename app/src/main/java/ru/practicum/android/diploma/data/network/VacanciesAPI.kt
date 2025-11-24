package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.dto.FilterAreaDto
import ru.practicum.android.diploma.data.network.dto.FilterIndustryDto
import ru.practicum.android.diploma.data.network.dto.VacancyDetailDto
import ru.practicum.android.diploma.data.network.dto.VacancyDto

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

    @GET("/vacancies")
    suspend fun getVacancy(
        @QueryMap filters: Map<String, @JvmSuppressWildcards Any>,
        @Header("Authorization") token: String = TOKEN,
        @Header("Content-Type") contentType: String = CONTENT_TYPE
    ): VacancyDto

    @GET("/vacancies/{id}")
    suspend fun getVacancyDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String = TOKEN,
        @Header("Content-Type") contentType: String = CONTENT_TYPE
    ): VacancyDetailDto

    companion object {
        private const val CONTENT_TYPE = "application/json"
        private const val TOKEN = "Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    }
}
