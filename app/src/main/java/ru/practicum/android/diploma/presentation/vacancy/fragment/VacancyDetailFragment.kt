package ru.practicum.android.diploma.presentation.vacancy.fragment

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
import ru.practicum.android.diploma.presentation.theme.AppTheme
import ru.practicum.android.diploma.presentation.vacancy.compose.VacancyDetailScreen
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewState

class VacancyDetailFragment : Fragment() {

    private val viewModel by viewModel<VacancyDetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val vacancyId = arguments?.getString("vacancyId")

        if (vacancyId != null) {
            viewModel.searchVacancyDetail(vacancyId)
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@VacancyDetailFragment
                )
            )

            setContent {
                AppTheme {
                    val state by viewModel.state.observeAsState()

                    VacancyDetailScreen(
                        state = state ?: VacancyDetailViewState.Loading,
                        onBackClick = {
                            findNavController().popBackStack()
                        }
                    )
                }
            }
        }
    }
}
