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
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.CountryScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.CountrySelectViewModel

class CountrySelectFragment : Fragment() {


    private val viewModel: CountrySelectViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@CountrySelectFragment
                )
            )

            setContent {
                CountryScreen(
                    onBackClick = { findNavController().popBackStack() }
                )

            }
        }
    }
}
