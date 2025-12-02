package ru.practicum.android.diploma.domain.favourites

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetailModel

class FavoriteVacancyInteractorImpl(
    private val repository: FavoriteVacancyRepository
) : FavoriteVacancyInteractor {
    override suspend fun add(vacancyDetailModel: VacancyDetailModel) {
        repository.add(vacancyDetailModel)
    }

    override suspend fun delete(id: String) {
        repository.delete(id)
    }

    override fun getAll(): Flow<List<VacancyDetailModel>> {
        return repository.getAll()
    }

    override suspend fun getById(id: String): VacancyDetailModel? {
        return repository.getById(id)
    }

    override suspend fun isFavorite(id: String): Boolean {
        return repository.isFavorite(id)
    }
}
