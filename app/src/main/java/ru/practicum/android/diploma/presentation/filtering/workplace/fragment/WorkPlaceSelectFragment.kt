package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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

                    // Создаем ключ для перерисовки
                    val uiKey = remember(tempSelection) {
                        "${tempSelection?.countryName}-${tempSelection?.regionName}"
                    }

                    // Отладочный вывод
                    LaunchedEffect(tempSelection) {
                        println("DEBUG: tempSelection changed to: $tempSelection")
                        println("DEBUG: UI key changed to: $uiKey")
                    }

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

                    // Вычисляем значения с remember - это заставит Compose пересчитать их
                    val currentCountry by remember(tempSelection, filterState) {
                        derivedStateOf {
                            tempSelection?.countryName ?: parseAreaString(filterState?.areaName).first
                        }
                    }

                    val currentRegion by remember(tempSelection, filterState) {
                        derivedStateOf {
                            tempSelection?.regionName ?: parseAreaString(filterState?.areaName).second
                        }
                    }

                    // Отладочный вывод для UI значений
                    LaunchedEffect(currentCountry, currentRegion) {
                        println("DEBUG: UI values - country: $currentCountry, region: $currentRegion")
                    }

                    WorkPlaceSelectScreen(
                        onBackClick = {
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
                        selectedCountry = if (tempSelection != null) {
                            tempSelection?.countryName
                        } else {
                            parseAreaString(filterState?.areaName).first
                        },
                        selectedRegion = if (tempSelection != null) {
                            if (tempSelection?.isRegionCleared == true) {
                                null
                            } else {
                                tempSelection?.regionName
                            }
                        } else {
                            parseAreaString(filterState?.areaName).second
                        },
                        onCountryClear = {
                            println("DEBUG: onCountryClear clicked")
                            viewModel.setTempCountry(null, null)
                        },
                        onRegionClear = {
                            println("DEBUG: onRegionClear clicked")
                            viewModel.setTempRegion(null, null)
                        },
                        onApplyClick = {
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
