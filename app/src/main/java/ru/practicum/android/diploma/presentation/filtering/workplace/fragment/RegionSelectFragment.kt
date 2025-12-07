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
                            // parentId - это ID страны, к которой относится регион
                            // areaId - ID региона
                            // areaName - название региона

                            if (parentId != null) {
                                // Это регион
                                filterViewModel.updateArea(areaId, areaName)
                            } else {
                                // Это страна
                                filterViewModel.updateArea(areaId, areaName)
                            }
                            findNavController().popBackStack()
                        },
                        regionState = state.value
                    )
                }
            }
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
