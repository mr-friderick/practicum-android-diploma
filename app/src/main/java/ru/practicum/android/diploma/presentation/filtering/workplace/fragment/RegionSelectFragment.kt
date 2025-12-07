package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.RegionScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.RegionSelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class RegionSelectFragment : Fragment() {

    private val viewModel: RegionSelectViewModel by viewModel()
    private val filterViewModel: FilterViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@RegionSelectFragment
                )
            )

            setContent {
                AppTheme {
                    val state = viewModel.state.observeAsState()

                    RegionScreen(
                        onBackClick = { findNavController().popBackStack() },
                        onAreaSelected = { areaId, areaName, parentId ->
                            onRegionSelected(areaId, areaName, parentId)
                        },
                        regionState = state.value
                    )
                }
            }
        }
    }

    private fun onRegionSelected(regionId: Int, regionName: String, parentId: Int?) {
        viewLifecycleOwner.lifecycleScope.launch {
            // Получаем текущий фильтр
            val currentFilter = filterViewModel.filterState.value

            when {
                currentFilter?.areaName != null && currentFilter.areaId != null -> {
                    // СЛУЧАЙ 1: Страна уже выбрана в фильтре
                    filterViewModel.updateRegion(
                        regionId = regionId,
                        regionName = regionName,
                        countryId = currentFilter.areaId,
                        countryName = currentFilter.areaName
                    )
                }
                parentId != null -> {
                    // СЛУЧАЙ 2: Страна не выбрана, но у региона есть parentId
                    val country = viewModel.getCountryByRegionId(parentId)

                    if (country != null) {
                        filterViewModel.updateRegion(
                            regionId = regionId,
                            regionName = regionName,
                            countryId = country.id,
                            countryName = country.name
                        )
                    } else {
                        filterViewModel.updateArea(regionId, regionName)
                    }
                }
                else -> {
                    // СЛУЧАЙ 3: У региона нет parentId
                    filterViewModel.updateArea(regionId, regionName)
                }
            }

            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRegions()
    }

    private fun loadRegions() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Получаем ID выбранной страны из фильтра
            val filterState = filterViewModel.filterState.value
            val countryId = filterState?.areaId

            // Загружаем регионы для выбранной страны
            viewModel.searchRegions(countryId ?: 0)
        }
    }
}
