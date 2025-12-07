package ru.practicum.android.diploma.data.localstorage

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.data.localstorage.dto.FilterDto

class SharedPrefLocalStorage(
    private val sharedPrefs: SharedPreferences,
    private val gson: Gson
) : LocalStorage {

    override fun save(filterDto: FilterDto) {
        sharedPrefs.edit {
            putString(
                KEY_FILTER,
                gson.toJson(filterDto)
            )
        }
    }

    override fun read(): FilterDto {
        val jsonString = sharedPrefs.getString(KEY_FILTER, "")
        return if (jsonString.isNullOrBlank()) {
            FilterDto()
        } else {
            gson.fromJson(jsonString, FilterDto::class.java) ?: FilterDto()
        }
    }

    override fun clear() {
        sharedPrefs.edit {
            remove(KEY_FILTER)
        }
    }

    companion object {
        private const val KEY_FILTER = "filter"
    }
}
