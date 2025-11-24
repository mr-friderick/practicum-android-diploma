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
        return gson.fromJson(
            sharedPrefs.getString(KEY_FILTER, ""),
            FilterDto::class.java
        )
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
