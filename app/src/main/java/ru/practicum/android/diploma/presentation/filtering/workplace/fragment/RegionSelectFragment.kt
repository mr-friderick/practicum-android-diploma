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
                // Если parentId == null, это СТРАНА
                // Сохраняем как страну
                workPlaceSelectViewModel.setTempCountry(regionName, regionId)
                println("DEBUG: Saved as country: $regionName (ID: $regionId)")
            } else {
                // Если parentId != null, это регион
                // Получаем страну для этого региона
                println("DEBUG: Getting country for parentId: $parentId")
                val country = viewModel.getCountryByRegionId(parentId)

                println("DEBUG: Country result: ${country?.name ?: "null"}")

                if (country != null) {
                    // Сохраняем как "Страна, Регион"
                    workPlaceSelectViewModel.setTempCountryAndRegionWithParent(
                        countryName = country.name,
                        countryId = country.id,
                        regionName = regionName,
                        regionId = regionId,
                        regionParentId = parentId
                    )
                    println("DEBUG: Saved as country+region: ${country.name}, $regionName")
                } else {
                    // Если страну не нашли, пытаемся получить по regionId
                    println("DEBUG: Trying to get country by regionId: $regionId")
                    val countryByRegion = viewModel.getCountryByRegionId(regionId)

                    if (countryByRegion != null) {
                        workPlaceSelectViewModel.setTempCountryAndRegionWithParent(
                            countryName = countryByRegion.name,
                            countryId = countryByRegion.id,
                            regionName = regionName,
                            regionId = regionId,
                            regionParentId = countryByRegion.id
                        )
                        println("DEBUG: Saved with country from regionId: ${countryByRegion.name}")
                    } else {
                        // Если совсем не нашли страну, сохраняем только регион
                        workPlaceSelectViewModel.setTempRegionWithParent(
                            regionName = regionName,
                            regionId = regionId,
                            regionParentId = parentId
                        )
                        println("DEBUG: Saved only region: $regionName")
                    }
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
