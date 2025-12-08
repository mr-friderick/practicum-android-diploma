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
                        regionState = state.value,
                        onSearchTextChanged = { searchText ->
                            // Добавляем обработку поиска если нужно
                        }
                    )
                }
            }
        }
    }

    private fun onRegionSelected(regionId: Int, regionName: String, parentId: Int?) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (parentId == null) {
                // Если parentId == null, это СТРАНА
                filterViewModel.updateCountry(regionId, regionName)
            } else {
                // Если parentId != null, это регион
                // Получаем страну для этого региона
                val country = viewModel.getCountryByRegionId(parentId)

                if (country != null) {
                    // Сохраняем как "Страна, Регион"
                    filterViewModel.updateRegion(
                        regionId = regionId,
                        regionName = regionName,
                        countryId = country.id,
                        countryName = country.name
                    )
                } else {
                    // Если страну не нашли, сохраняем только регион
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

            if (filterState?.areaId != null) {
                // Если в фильтре уже выбрана страна, загружаем только ее регионы
                viewModel.searchRegions(filterState.areaId!!)
            } else {
                // Иначе загружаем все регионы
                viewModel.searchRegions()
            }
        }
    }
}
