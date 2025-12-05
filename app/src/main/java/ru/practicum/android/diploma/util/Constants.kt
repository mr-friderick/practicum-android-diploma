package ru.practicum.android.diploma.util

/**
 * Файл для констант в проекте.
 * Можно использовать для Shared Prefs и Прочих констант.
 * Хранение констант в одном месте
 */

object Constants {
    const val VACANCY_BASE_URL = "https://practicum-diploma-8bc38133faba.herokuapp.com/"
    const val FILE_PREFERENCES = "local_preferences"

    // Fragment Result keys for filter selection
    const val REQUEST_KEY_AREA = "area_selection_result"
    const val REQUEST_KEY_INDUSTRY = "industry_selection_result"
    const val KEY_AREA_ID = "area_id"
    const val KEY_AREA_NAME = "area_name"
    const val KEY_INDUSTRY_ID = "industry_id"
    const val KEY_INDUSTRY_NAME = "industry_name"
}
