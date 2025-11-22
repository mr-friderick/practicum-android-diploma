package ru.practicum.android.diploma.presentation.vacancy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.presentation.vacancy.compose.VacancyDetailScreen
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel

class VacancyDetailFragment : Fragment() {

    private val viewModel: VacancyDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@VacancyDetailFragment
                )
            )

            setContent {
                VacancyDetailScreen {
                    findNavController().popBackStack()
                }
            }
        }
    }
}
