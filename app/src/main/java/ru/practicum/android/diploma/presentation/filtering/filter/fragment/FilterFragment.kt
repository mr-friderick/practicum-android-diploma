package ru.practicum.android.diploma.presentation.filtering.filter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.filter.compose.FilterScreen
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class FilterFragment : Fragment() {

    private val viewModel: FilterViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@FilterFragment
                )
            )

            setContent {
                AppTheme {
                    val filterState by viewModel.filterState.collectAsState()

                    FilterScreen(
                        areaName = filterState?.areaName,
                        industryName = filterState?.industryName,
                        salary = filterState?.salary?.toString() ?: "",
                        onlyWithSalary = filterState?.onlyWithSalary ?: false,
                        onBackClick = {
                            viewModel.onBackClick()
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
                        onWorkPlaceClear = {
                            viewModel.updateArea(null, null)
                        },
                        onIndustryClear = {
                            viewModel.updateIndustry(null, null)
                        },
                        onWorkPlaceSelect = {
                            findNavController()
                                .navigate(R.id.action_filterFragment_to_workPlaceSelectFragment)
                        },
                        onIndustrySelect = {
                            findNavController()
                                .navigate(R.id.action_filterFragment_to_industrySelectFragment2)
                        },
                        onSalaryChange = { salaryText ->
                            val salary = salaryText.toIntOrNull()
                            viewModel.updateSalary(salary)
                        },
                        onOnlyWithSalaryChange = { checked ->
                            viewModel.updateOnlyWithSalary(checked)
                        },
                        onApplyClick = {
                            viewModel.applyFilter()
                            findNavController().popBackStack()
                        },
                        onResetClick = {
                            viewModel.resetFilter()
                        }
                    )
                }
            }
        }
    }
}
