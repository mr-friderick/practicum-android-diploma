package ru.practicum.android.diploma.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancies")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,

    // Основные поля для списка
    val employerLogoUrl: String?,
    val vacancyName: String,
    val region: String,
    val companyName: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val currency: String?,

    // Детальные поля
    val description: String,
    val experience: String?,
    val schedule: String?,
    val employment: String?,
    val contacts: String?, // JSON строку или отдельную таблицу
    val skills: String, // JSON массив навыков
    val vacancyUrl: String,
    val industry: String,

    // Технические поля
    val addedToFavoritesAt: Long = System.currentTimeMillis()
)
