package ru.practicum.android.diploma.util.temp_package_for_screen.fragments

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
import ru.practicum.android.diploma.util.temp_package_for_screen.screens.WorkPlaceSelectScreen
import ru.practicum.android.diploma.util.temp_package_for_screen.viewmodels.WorkPlaceSelectViewModel

class WorkPlaceSelectFragment : Fragment() {


    private val viewModel: WorkPlaceSelectViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@WorkPlaceSelectFragment
                )
            )

            setContent {
                WorkPlaceSelectScreen (onBackClick = {findNavController().popBackStack()},
                    onCountryClick = {findNavController()
                        .navigate(R.id.action_workPlaceSelectFragment_to_countrySelectFragment)},
                    onRegionClick = {findNavController()
                        .navigate(R.id.action_workPlaceSelectFragment_to_regionSelectFragment) })
            }
        }
    }
}
