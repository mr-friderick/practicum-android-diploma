package ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filtering.FilterInteractor
import ru.practicum.android.diploma.domain.models.FilterAreaModel
import ru.practicum.android.diploma.domain.models.SearchState

class RegionSelectViewModel(
    private val filterInteractor: FilterInteractor
) : ViewModel() {

    private val _state = MutableLiveData<RegionViewState>()
    val state: MutableLiveData<RegionViewState> = _state

    fun searchRegions(idCountry: Int = 0) {
        viewModelScope.launch {
            _state.postValue(RegionViewState.Loading)

            val flowResult = if (idCountry != 0) {
                filterInteractor.findRegionsByCountry(idCountry)
            } else {
                filterInteractor.searchRegions()
            }

            flowResult.collect { state ->
                when (state) {
                    is SearchState.Success<List<FilterAreaModel>> -> {
                        // Собираем все регионы для отображения
                        val allRegions = collectAllRegionsForFilter(state.data)

                        // Сортируем по алфавиту
                        val sortedRegions = allRegions.sortedBy { it.name }

                        _state.postValue(RegionViewState.Region(sortedRegions))
                    }

                    is SearchState.NoInternet -> {
                        _state.postValue(RegionViewState.NoInternet)
                    }

                    is SearchState.Error -> {
                        _state.postValue(RegionViewState.Error(state.message))
                    }

                    else -> { /* Остальные стейты не требуют обработки */ }
                }
            }
        }
    }

    private fun collectAllRegionsForFilter(regions: List<FilterAreaModel>): List<FilterAreaModel> {
        val result = mutableListOf<FilterAreaModel>()

        // Рекурсивно обходим все регионы
        fun collectAll(area: FilterAreaModel) {
            // Если это не страна (parentId != null), добавляем в результат
            if (area.parentId != null) {
                result.add(area.copy(areas = emptyList()))
            }

            // Рекурсивно обрабатываем дочерние элементы
            area.areas.forEach { child ->
                collectAll(child)
            }
        }

        // Начинаем обход с корневых элементов
        regions.forEach { region ->
            collectAll(region)
        }

        return result
    }

    suspend fun getCountryByRegionId(regionId: Int): FilterAreaModel? {
        var result: FilterAreaModel? = null

        filterInteractor.findCountryByRegion(regionId).collectLatest { searchState ->
            when (searchState) {
                is SearchState.Success<FilterAreaModel?> -> {
                    result = searchState.data
                }
                // В случае ошибки просто возвращаем null
                else -> {
                    result = null
                }
            }
        }

        return result
    }
}
