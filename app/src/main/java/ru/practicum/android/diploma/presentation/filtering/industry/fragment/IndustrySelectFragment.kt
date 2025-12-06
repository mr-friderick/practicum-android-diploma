package ru.practicum.android.diploma.presentation.filtering.industry.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filtering.industry.compose.IndustryScreen
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustrySelectViewModel

class IndustrySelectFragment : Fragment() {

    private val viewModel by viewModel<IndustrySelectViewModel>()

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
                IndustryScreen(onBackClick = { findNavController().popBackStack() })
            }
        }
    }
}
