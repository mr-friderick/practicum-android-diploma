package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.RegionScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.RegionSelectViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.WorkPlaceSelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class RegionSelectFragment : Fragment() {

    private val viewModel: RegionSelectViewModel by viewModel()
    private val filterViewModel: FilterViewModel by viewModel(ownerProducer = { requireActivity() })
    private val workPlaceSelectViewModel: WorkPlaceSelectViewModel by activityViewModels()

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
                        onBackClick = {
                            findNavController().popBackStack()
                        },
                        onAreaSelected = { areaId, areaName, parentId ->
                            onRegionSelected(areaId, areaName, parentId)
                        },
                        regionState = state.value,
                        onSearchTextChanged = { searchText ->
                            // Обработка поиска
                        }
                    )
                }
            }
        }
    }

    private fun onRegionSelected(regionId: Int, regionName: String, parentId: Int?) {
        viewLifecycleOwner.lifecycleScope.launch {
            if (parentId == null) {
                // Это страна - сохраняем во временное хранилище
                workPlaceSelectViewModel.setTempCountry(regionName, regionId)
            } else {
                // Это регион - получаем страну
                val country = viewModel.getCountryByRegionId(parentId)

                if (country != null) {
                    // Сохраняем и страну и регион во временное хранилище
                    workPlaceSelectViewModel.setTempCountryAndRegion(
                        countryName = country.name,
                        countryId = country.id,
                        regionName = regionName,
                        regionId = regionId
                    )
                } else {
                    // Если страну не нашли, сохраняем только регион
                    workPlaceSelectViewModel.setTempRegion(regionName, regionId)
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
            // СТАРАЯ ЛОГИКА, но используем временное хранилище вместо FilterViewModel

            // Получаем ID страны из временного хранилища
            val countryIdFromTemp = workPlaceSelectViewModel.getTempCountryId()

            // Также проверяем FilterViewModel на случай, если нужно сохранить обратную совместимость
            val filterState = filterViewModel.filterState.value
            val countryIdFromFilter = filterState?.areaId

            if (countryIdFromTemp != null) {
                // Если во временном хранилище выбрана страна, загружаем только ее регионы
                viewModel.searchRegions(countryIdFromTemp)
            } else if (countryIdFromFilter != null) {
                // Если в фильтре выбрана страна, тоже загружаем ее регионы
                viewModel.searchRegions(countryIdFromFilter)
            } else {
                // Иначе загружаем все регионы
                viewModel.searchRegions()
            }
        }
    }
}

