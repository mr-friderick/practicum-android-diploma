package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

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
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.WorkPlaceSelectScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.WorkPlaceSelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class WorkPlaceSelectFragment : Fragment() {

    private val viewModel: WorkPlaceSelectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@WorkPlaceSelectFragment
                )
            )

            setContent {
                AppTheme {
                    WorkPlaceSelectScreen(
                        onBackClick = { findNavController().popBackStack() },
                        onCountryClick = {
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_countrySelectFragment)
                        },
                        onRegionClick = {
                            findNavController()
                                .navigate(R.id.action_workPlaceSelectFragment_to_regionSelectFragment)
                        }
                    )
                }

            }
        }
    }
}
