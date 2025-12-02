package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.database.entity.FavoriteVacancyEntity

interface FavoriteRepository {
    fun getAll(): Flow<List<FavoriteVacancyEntity>>
    suspend fun getById(id: String): FavoriteVacancyEntity?
    suspend fun isFavorite(id: String): Boolean
    suspend fun add(entity: FavoriteVacancyEntity)
    suspend fun remove(entity: FavoriteVacancyEntity)
    suspend fun removeById(id: String)
}
