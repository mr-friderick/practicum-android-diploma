package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.WorkPlaceSelectScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.WorkPlaceSelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class WorkPlaceSelectFragment : Fragment() {

    private val viewModel: WorkPlaceSelectViewModel by activityViewModels()
    private val filterViewModel: FilterViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@WorkPlaceSelectFragment
                )
            )

            setContent {
                AppTheme {
                    val filterState by filterViewModel.filterState.collectAsState()
                    val tempSelection by viewModel.tempSelection.observeAsState()

                    // ПРИ ИНИЦИАЛИЗАЦИИ: загружаем из фильтра во временное хранилище
                    if (tempSelection?.countryName == null && tempSelection?.regionName == null) {
                        val (filterCountry, filterRegion) = parseAreaString(filterState?.areaName)

                        // Здесь нужны ID стран/регионов из фильтра
                        // Пока сохраняем только имена, ID могут быть null
                        if (filterCountry != null || filterRegion != null) {
                            viewModel.setTempCountryAndRegion(
                                countryName = filterCountry,
                                countryId = filterState?.areaId, // Берем ID из фильтра
                                regionName = filterRegion,
                                regionId = null // ID региона сложнее получить
                            )
                        }
                    }

                    // Используем временные значения
                    val currentCountry = tempSelection?.countryName
                    val currentRegion = tempSelection?.regionName

                    WorkPlaceSelectScreen(
                        onBackClick = {
                            // При нажатии назад - очищаем временное хранилище
                            viewModel.clearTempSelection()
                            findNavController().popBackStack()
                        },
                        onCountryClick = {
                            // Сохраняем текущий регион перед переходом
                            val currentRegionName = tempSelection?.regionName
                            val currentRegionId = tempSelection?.regionId
                            viewModel.setTempRegion(currentRegionName, currentRegionId)
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_countrySelectFragment)
                        },
                        onRegionClick = {
                            // Сохраняем текущую страну перед переходом
                            val currentCountryName = tempSelection?.countryName
                            val currentCountryId = tempSelection?.countryId
                            viewModel.setTempCountry(currentCountryName, currentCountryId)
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_regionSelectFragment)
                        },
                        selectedCountry = currentCountry,
                        selectedRegion = currentRegion,
                        onCountryClear = {
                            // Очищаем только страну
                            val currentRegionName = tempSelection?.regionName
                            val currentRegionId = tempSelection?.regionId
                            viewModel.setTempCountryAndRegion(
                                countryName = null,
                                countryId = null,
                                regionName = currentRegionName,
                                regionId = currentRegionId
                            )
                        },
                        onRegionClear = {
                            // Очищаем только регион
                            val currentCountryName = tempSelection?.countryName
                            val currentCountryId = tempSelection?.countryId
                            viewModel.setTempCountryAndRegion(
                                countryName = currentCountryName,
                                countryId = currentCountryId,
                                regionName = null,
                                regionId = null
                            )
                        },
                        onApplyClick = {
                            // Сохраняем из временного хранилища в FilterViewModel
                            applyTempSelectionToFilter(
                                countryName = tempSelection?.countryName,
                                countryId = tempSelection?.countryId,
                                regionName = tempSelection?.regionName,
                                regionId = tempSelection?.regionId
                            )
                        },
                        showApplyButton = viewModel.hasSelection()
                    )
                }
            }
        }
    }

    private fun applyTempSelectionToFilter(
        countryName: String?,
        countryId: Int?,
        regionName: String?,
        regionId: Int?
    ) {
        // Формируем строку для сохранения в фильтр
        val areaName = when {
            countryName != null && regionName != null -> "$countryName, $regionName"
            countryName != null -> countryName
            regionName != null -> regionName
            else -> null
        }

        // Используем ID региона если есть, иначе ID страны
        val areaId = regionId ?: countryId

        filterViewModel.updateArea(areaId, areaName)
        viewModel.clearTempSelection()
        findNavController().popBackStack()
    }

    // Функция для парсинга строки "Страна, Регион"
    private fun parseAreaString(areaString: String?): Pair<String?, String?> {
        return if (areaString != null) {
            val parts = areaString.split(", ")
            when (parts.size) {
                1 -> Pair(areaString, null)
                2 -> Pair(parts[0], parts[1])
                else -> Pair(areaString, null)
            }
        } else {
            Pair(null, null)
        }
    }
}
