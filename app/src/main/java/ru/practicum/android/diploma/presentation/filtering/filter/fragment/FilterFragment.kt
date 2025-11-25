package ru.practicum.android.diploma.presentation.filtering.filter.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.filtering.filter.compose.FilterScreen
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel

class FilterFragment : Fragment() {

    private val viewModel: FilterViewModel by viewModels()

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
                FilterScreen(
                    onBackClick = { findNavController().popBackStack() },
                    onWorkPlaceClick = {
                        findNavController()
                            .navigate(R.id.action_filterFragment_to_workPlaceSelectFragment)
                    },
                    onIndustryClick = {
                        findNavController()
                            .navigate(R.id.action_filterFragment_to_industrySelectFragment2)
                    }
                )
            }
        }
    }
}
