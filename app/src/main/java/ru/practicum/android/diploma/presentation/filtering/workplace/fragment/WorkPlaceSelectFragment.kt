package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.WorkPlaceSelectScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.WorkPlaceSelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class WorkPlaceSelectFragment : Fragment() {

    private val viewModel: WorkPlaceSelectViewModel by viewModels()
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
                    val filterState = filterViewModel.filterState.collectAsState()

                    // Парсим строку для раздельного отображения
                    val areaString = filterState.value?.areaName
                    val (selectedCountry, selectedRegion) = parseAreaString(areaString)

                    WorkPlaceSelectScreen(
                        onBackClick = { findNavController().popBackStack() },
                        onCountryClick = {
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_countrySelectFragment)
                        },
                        onRegionClick = {
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_regionSelectFragment)
                        },
                        selectedCountry = selectedCountry,
                        selectedRegion = selectedRegion,
                        onCountryClear = {
                            // Очищаем место работы полностью
                            filterViewModel.updateArea(null, null)
                        },
                        onRegionClear = {
                            // Если был только регион - очищаем, если была страна - оставляем только страну
                            filterViewModel.updateArea(null, selectedCountry)
                        },
                        onApplyClick = {
                            // Просто возвращаемся назад - изменения уже сохранены
                            findNavController().popBackStack()
                        }
                    )
                }

            }
        }
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
