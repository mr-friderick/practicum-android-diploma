package ru.practicum.android.diploma.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_vacancies")
data class FavoriteVacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val salary: String? = null,
    val address: String? = null,
    val experience: String? = null,
    val schedule: String? = null,
    val employment: String? = null,
    val contacts: String? = null,
    val employer: String,
    val area: String,
    val skills: String? = null,
    val url: String,
    val industry: String? = null,
    val addedToFavoritesAt: Long = System.currentTimeMillis()
)
