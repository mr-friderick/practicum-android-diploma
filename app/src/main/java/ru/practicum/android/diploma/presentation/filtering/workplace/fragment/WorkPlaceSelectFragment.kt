package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.WorkPlaceSelectScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.WorkPlaceSelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class WorkPlaceSelectFragment : Fragment() {

    private val viewModel: WorkPlaceSelectViewModel by viewModel(ownerProducer = { requireActivity() })
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

                    // Инициализация: загружаем данные из фильтра только если временное хранилище пустое
                    LaunchedEffect(filterState) {
                        if (!viewModel.hasSelection()) {
                            val (filterCountry, filterRegion) = parseAreaString(filterState?.areaName)

                            if (filterCountry != null || filterRegion != null) {
                                viewModel.setTempCountryAndRegion(
                                    countryName = filterCountry,
                                    countryId = filterState?.areaId,
                                    regionName = filterRegion,
                                    regionId = null
                                )
                            }
                        }
                    }

                    // Используем временные значения
                    val currentCountry = tempSelection?.countryName ?: parseAreaString(filterState?.areaName).first
                    val currentRegion = tempSelection?.regionName ?: parseAreaString(filterState?.areaName).second

                    WorkPlaceSelectScreen(
                        onBackClick = {
                            // Сохраняем изменения перед уходом
                            saveToFilter()
                            findNavController().popBackStack()
                        },
                        onCountryClick = {
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_countrySelectFragment)
                        },
                        onRegionClick = {
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_regionSelectFragment)
                        },
                        selectedCountry = currentCountry,
                        selectedRegion = currentRegion,
                        onCountryClear = {
                            // Очищаем только страну
                            val currentRegionName = tempSelection?.regionName
                            val currentRegionId = tempSelection?.regionId
                            val currentRegionParentId = tempSelection?.regionParentId

                            if (currentRegionName != null) {
                                if (currentRegionParentId != null) {
                                    viewModel.setTempRegionWithParent(
                                        regionName = currentRegionName,
                                        regionId = currentRegionId,
                                        regionParentId = currentRegionParentId
                                    )
                                } else {
                                    viewModel.setTempRegion(
                                        regionName = currentRegionName,
                                        regionId = currentRegionId
                                    )
                                }
                            } else {
                                viewModel.clearTempSelection()
                            }
                        },
                        onRegionClear = {
                            // Очищаем только регион
                            val currentCountryName = tempSelection?.countryName
                            val currentCountryId = tempSelection?.countryId

                            if (currentCountryName != null) {
                                viewModel.setTempCountry(
                                    countryName = currentCountryName,
                                    countryId = currentCountryId
                                )
                            } else {
                                viewModel.clearTempSelection()
                            }
                        },
                        onApplyClick = {
                            // Сохраняем и возвращаемся
                            saveToFilter()
                            findNavController().popBackStack()
                        },
                        showApplyButton = viewModel.hasSelection()
                    )
                }
            }
        }
    }

    private fun saveToFilter() {
        val tempSelection = viewModel.tempSelection.value
        val countryName = tempSelection?.countryName
        val countryId = tempSelection?.countryId
        val regionName = tempSelection?.regionName
        val regionId = tempSelection?.regionId

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
