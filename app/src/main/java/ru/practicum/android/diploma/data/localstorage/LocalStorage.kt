package ru.practicum.android.diploma.data.localstorage

import ru.practicum.android.diploma.data.localstorage.dto.FilterDto

interface LocalStorage {
    fun save(filterDto: FilterDto)

    fun read(): FilterDto

    fun clear()
}
