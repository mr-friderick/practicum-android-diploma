package ru.practicum.android.diploma.util.temp_package_for_screen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.practicum.android.diploma.util.temp_package_for_screen.screens.TeamScreen
import ru.practicum.android.diploma.util.temp_package_for_screen.viewmodels.TeamViewModel

class TeamFragment : Fragment() {

    private val viewModel: TeamViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@TeamFragment
                )
            )

            setContent {
                TeamScreen()

            }
        }
    }
}
