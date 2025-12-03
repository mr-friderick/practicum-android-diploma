package ru.practicum.android.diploma.domain.favourites

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailModel

interface FavoriteVacancyRepository {
    suspend fun add(vacancyDetailModel: VacancyDetailModel)

    suspend fun delete(id: String)

    fun getAll(): Flow<List<VacancyDetailModel>>

    suspend fun getById(id: String): VacancyDetailModel?

    suspend fun isFavorite(id: String): Boolean
}
