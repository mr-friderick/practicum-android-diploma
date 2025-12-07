package ru.practicum.android.diploma.presentation.filtering.industry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.industry.compose.IndustryScreen
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustrySelectViewModel
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustryViewState
import ru.practicum.android.diploma.presentation.theme.AppTheme

class IndustrySelectFragment : Fragment() {

    private val viewModel by viewModel<IndustrySelectViewModel>()
    private val filterViewModel: FilterViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.searchIndustries()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@IndustrySelectFragment
                )
            )

            setContent {
                AppTheme {
                    val viewState by viewModel.state.observeAsState(IndustryViewState.Loading)

                    IndustryScreen(
                        viewState = viewState,
                        onBackClick = { findNavController().popBackStack() },
                        onIndustrySelected = { industryId, industryName ->
                            filterViewModel.updateIndustry(industryId, industryName)
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
