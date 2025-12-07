package ru.practicum.android.diploma.presentation.filtering.industry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.industry.compose.IndustryScreen
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustrySelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class IndustrySelectFragment : Fragment() {

    private val viewModel: IndustrySelectViewModel by viewModel()
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@IndustrySelectFragment
                )
            )

            setContent {
                AppTheme {
                    IndustryScreen(
                        viewModel = viewModel,
                        onBackClick = { findNavController().popBackStack() },
                        onIndustrySelected = { selectedIndustry ->
                            // Обновляем фильтры в shared ViewModel
                            filterViewModel.updateIndustry(selectedIndustry)
                            // Возвращаемся назад
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
