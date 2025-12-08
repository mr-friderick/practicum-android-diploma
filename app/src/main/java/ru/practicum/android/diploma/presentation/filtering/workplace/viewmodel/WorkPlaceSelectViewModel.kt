package ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WorkPlaceSelectViewModel : ViewModel() {

    data class TempSelection(
        val countryName: String? = null,
        val countryId: Int? = null,
        val regionName: String? = null,
        val regionId: Int? = null,
        val regionParentId: Int? = null
    )

    // Временное хранилище для выбранных значений
    private val _tempSelection = MutableLiveData<TempSelection>(TempSelection())
    val tempSelection: LiveData<TempSelection> = _tempSelection

    // Сохраняем временные значения
    fun setTempCountry(countryName: String?, countryId: Int?) {
        val current = _tempSelection.value ?: TempSelection()
        _tempSelection.value = current.copy(
            countryName = countryName,
            countryId = countryId,
            // При смене страны - очищаем регион
            regionName = null,
            regionId = null,
            regionParentId = null
        )
    }

    fun setTempCountryAndRegion(
        countryName: String?,
        countryId: Int?,
        regionName: String?,
        regionId: Int?
    ) {
        _tempSelection.value = TempSelection(
            countryName = countryName,
            countryId = countryId,
            regionName = regionName,
            regionId = regionId,
            regionParentId = null
        )
    }

    fun setTempCountryAndRegionWithParent(
        countryName: String?,
        countryId: Int?,
        regionName: String?,
        regionId: Int?,
        regionParentId: Int?
    ) {
        _tempSelection.value = TempSelection(
            countryName = countryName,
            countryId = countryId,
            regionName = regionName,
            regionId = regionId,
            regionParentId = regionParentId
        )
    }

    fun setTempRegion(regionName: String?, regionId: Int?) {
        val current = _tempSelection.value ?: TempSelection()
        _tempSelection.value = current.copy(
            regionName = regionName,
            regionId = regionId,
            regionParentId = null
        )
    }

    fun setTempRegionWithParent(
        regionName: String?,
        regionId: Int?,
        regionParentId: Int?
    ) {
        val current = _tempSelection.value ?: TempSelection()
        _tempSelection.value = current.copy(
            regionName = regionName,
            regionId = regionId,
            regionParentId = regionParentId
        )
    }

    // Сбрасываем временные значения
    fun clearTempSelection() {
        _tempSelection.value = TempSelection()
    }

    // Геттеры
    fun getTempCountryName(): String? = _tempSelection.value?.countryName
    fun getTempCountryId(): Int? = _tempSelection.value?.countryId
    fun getTempRegionName(): String? = _tempSelection.value?.regionName
    fun getTempRegionId(): Int? = _tempSelection.value?.regionId
    fun getTempRegionParentId(): Int? = _tempSelection.value?.regionParentId

    // Проверка наличия выбора
    fun hasSelection(): Boolean {
        val current = _tempSelection.value
        return current?.countryName != null || current?.regionName != null
    }

    override fun onCleared() {
        super.onCleared()
    }
}
