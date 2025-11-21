package ru.practicum.android.diploma.util.temp_package_for_screen.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.util.temp_package_for_screen.fragments.RegionSelectFragment
import ru.practicum.android.diploma.util.temp_package_for_screen.screens.CountryScreen
import ru.practicum.android.diploma.util.temp_package_for_screen.screens.RegionScreen
import ru.practicum.android.diploma.util.temp_package_for_screen.viewmodels.CountrySelectViewModel

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
