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
        val regionParentId: Int? = null,
        val isRegionCleared: Boolean = false
    )

    // Временное хранилище для выбранных значений
    private val _tempSelection = MutableLiveData<TempSelection>(TempSelection())
    val tempSelection: LiveData<TempSelection> = _tempSelection

    fun setTempCountry(countryName: String?, countryId: Int?) {
        println("DEBUG ViewModel: setTempCountry called with: name=$countryName, id=$countryId")

        if (countryName == null) {
            // При удалении страны очищаем ВСЁ
            _tempSelection.value = TempSelection()
            println("DEBUG ViewModel: Cleared all temp selection")
        } else {
            // При установке новой страны - очищаем регион и сбрасываем флаг
            _tempSelection.value = TempSelection(
                countryName = countryName,
                countryId = countryId,
                isRegionCleared = false
            )
            println("DEBUG ViewModel: New temp selection: ${_tempSelection.value}")
        }
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
        if (regionName == null) {
            // При удалении региона, сохраняем страну и ставим флаг
            val current = _tempSelection.value ?: TempSelection()
            _tempSelection.value = current.copy(
                regionName = null,
                regionId = null,
                regionParentId = null,
                isRegionCleared = true // флаг что регион специально очищен
            )
        } else {
            val current = _tempSelection.value ?: TempSelection()
            _tempSelection.value = current.copy(
                regionName = regionName,
                regionId = regionId,
                regionParentId = null,
                isRegionCleared = false // сбрасываем флаг
            )
        }
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

    fun clearTempSelection() {
        println("DEBUG ViewModel: clearTempSelection called")
        _tempSelection.value = TempSelection() // isRegionCleared будет false по умолчанию
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
