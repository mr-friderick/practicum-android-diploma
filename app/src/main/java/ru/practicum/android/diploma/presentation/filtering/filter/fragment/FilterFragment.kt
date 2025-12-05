package ru.practicum.android.diploma.presentation.filtering.filter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.filter.compose.FilterScreen
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel

class FilterFragment : Fragment() {

    private val filterViewModel: FilterViewModel by viewModel()
    private val searchViewModel: SearchViewModel by viewModel()

    companion object {
        const val REQUEST_KEY_AREA = "area_selection_result"
        const val REQUEST_KEY_INDUSTRY = "industry_selection_result"
        const val KEY_AREA_ID = "area_id"
        const val KEY_AREA_NAME = "area_name"
        const val KEY_INDUSTRY_ID = "industry_id"
        const val KEY_INDUSTRY_NAME = "industry_name"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupFragmentResultListeners()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@FilterFragment
                )
            )

            setContent {
                /**
                 * Это стейт для логики показа кнопок !!!
                 */
                val currentFilter by filterViewModel.currentFilter.collectAsState()
                val showResetButton by filterViewModel.showResetButton.collectAsState()
                val showApplyButton by filterViewModel.showApplyButton.collectAsState()

                FilterScreen(

                    onBackClick = {
                        filterViewModel.onBackClick()
                        findNavController().popBackStack()
                    },
                    onWorkPlaceClick = {
                        findNavController()
                            .navigate(R.id.action_filterFragment_to_workPlaceSelectFragment)
                    },
                    onIndustryClick = {
                        findNavController()
                            .navigate(R.id.action_filterFragment_to_industrySelectFragment2)
                    },
                )
            }
        }
    }

    private fun setupFragmentResultListeners() {
        // Ожидаем результат выбора места работы
        setFragmentResultListener(REQUEST_KEY_AREA) { _, bundle ->
            val areaId = bundle.getInt(KEY_AREA_ID, -1).takeIf { it != -1 }
            val areaName = bundle.getString(KEY_AREA_NAME)
            filterViewModel.updateArea(areaId, areaName)
        }

        // Ожидаем результат выбора отрасли
        setFragmentResultListener(REQUEST_KEY_INDUSTRY) { _, bundle ->
            val industryId = bundle.getInt(KEY_INDUSTRY_ID, -1).takeIf { it != -1 }
            val industryName = bundle.getString(KEY_INDUSTRY_NAME)
            filterViewModel.updateIndustry(industryId, industryName)
        }
    }
}
