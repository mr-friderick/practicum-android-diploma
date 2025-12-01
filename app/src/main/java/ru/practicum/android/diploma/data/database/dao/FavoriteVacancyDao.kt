package ru.practicum.android.diploma.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.database.entity.FavoriteVacancyEntity

@Dao
interface FavoriteVacancyDao {
    @Query("SELECT * FROM favorite_vacancies ORDER BY addedToFavoritesAt DESC")
    fun getAll(): Flow<List<FavoriteVacancyEntity>>

    @Query("SELECT * FROM favorite_vacancies WHERE id = :id")
    suspend fun getById(id: String): FavoriteVacancyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vacancy: FavoriteVacancyEntity)

    @Query("DELETE FROM favorite_vacancies WHERE id = :vacancyId")
    suspend fun delete(vacancyId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancies WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}
