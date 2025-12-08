package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
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
    // Добавляем WorkPlaceSelectViewModel для временного хранения
    private val workPlaceSelectViewModel: WorkPlaceSelectViewModel by viewModel(ownerProducer = { requireActivity() })

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
            println("DEBUG: Region selected - ID: $regionId, Name: $regionName, ParentID: $parentId")

            if (parentId == null) {
                workPlaceSelectViewModel.setTempCountry(regionName, regionId)
            } else {
                val country = viewModel.getCountryByRegionId(parentId)

                if (country != null) {
                    workPlaceSelectViewModel.setTempCountryAndRegionWithParent(
                        countryName = country.name,
                        countryId = country.id,
                        regionName = regionName,
                        regionId = regionId,
                        regionParentId = parentId
                    )
                } else {
                    val countryByRegion = viewModel.getCountryByRegionId(regionId)

                    if (countryByRegion != null) {
                        workPlaceSelectViewModel.setTempCountryAndRegionWithParent(
                            countryName = countryByRegion.name,
                            countryId = countryByRegion.id,
                            regionName = regionName,
                            regionId = regionId,
                            regionParentId = countryByRegion.id
                        )
                    } else {
                        workPlaceSelectViewModel.setTempRegionWithParent(
                            regionName = regionName,
                            regionId = regionId,
                            regionParentId = parentId
                        )
                    }
                }
            }

            // Возвращаемся на экран выбора места работы
            findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadRegions()
    }

    private fun loadRegions() {
        viewLifecycleOwner.lifecycleScope.launch {
            // Получаем ID страны из временного хранилища WorkPlaceSelectViewModel
            val countryIdFromTemp = workPlaceSelectViewModel.getTempCountryId()

            if (countryIdFromTemp != null) {
                // Если выбрана страна, загружаем ее регионы
                println("DEBUG: Loading regions for country ID: $countryIdFromTemp")
                viewModel.searchRegions(countryIdFromTemp)
            } else {
                // Иначе загружаем все регионы
                println("DEBUG: Loading all regions")
                viewModel.searchRegions()
            }
        }
    }
}
