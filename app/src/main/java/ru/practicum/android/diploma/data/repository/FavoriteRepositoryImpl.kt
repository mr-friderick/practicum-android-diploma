package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.database.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.data.database.entity.FavoriteVacancyEntity

class FavoriteRepositoryImpl(
    private val dao: FavoriteVacancyDao
) : FavoriteRepository {
    override fun getAll(): Flow<List<FavoriteVacancyEntity>> = dao.getAll()
    override suspend fun getById(id: String): FavoriteVacancyEntity? = dao.getById(id)
    override suspend fun isFavorite(id: String): Boolean = dao.isFavorite(id)
    override suspend fun add(entity: FavoriteVacancyEntity) = dao.insert(entity)
    override suspend fun remove(entity: FavoriteVacancyEntity) = dao.delete(entity)
    override suspend fun removeById(id: String) {
        dao.getById(id)?.let { dao.delete(it) }
    }
}
