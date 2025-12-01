package ru.practicum.android.diploma.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.database.converter.FavoriteVacancyConverter
import ru.practicum.android.diploma.data.database.dao.FavoriteVacancyDao
import ru.practicum.android.diploma.domain.favourites.FavoriteVacancyRepository
import ru.practicum.android.diploma.domain.models.VacancyDetailModel

class FavoriteVacancyRepositoryImpl(
    private val dao: FavoriteVacancyDao,
    private val converter: FavoriteVacancyConverter
): FavoriteVacancyRepository {
    override suspend fun add(vacancyDetailModel: VacancyDetailModel) = withContext(Dispatchers.IO) {
        dao.insert(
            converter.map(vacancyDetailModel)
        )
    }

    override suspend fun delete(id: String) = withContext(Dispatchers.IO) {
        dao.delete(id)
    }

    override fun getAll(): Flow<List<VacancyDetailModel>> {
        return dao.getAll()
            .map { entities -> entities.map { converter.map(it) } }
    }

    override suspend fun getById(id: String): VacancyDetailModel? = withContext(Dispatchers.IO) {
        dao.getById(id)?.let { converter.map(it) }
    }

    override suspend fun isFavorite(id: String): Boolean = withContext(Dispatchers.IO) {
        dao.isFavorite(id)
    }
}
