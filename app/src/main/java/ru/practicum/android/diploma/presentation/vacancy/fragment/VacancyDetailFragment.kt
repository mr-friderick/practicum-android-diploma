package ru.practicum.android.diploma.presentation.vacancy.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is VacancyDetailViewState.Loading -> {
                    Toast.makeText(
                        requireContext(),
                        "Loading",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is VacancyDetailViewState.NotFound -> {
                    Toast.makeText(
                        requireContext(),
                        "NotFound",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is VacancyDetailViewState.VacancyDetail -> {
                    Toast.makeText(
                        requireContext(),
                        "VacancyDetail",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is VacancyDetailViewState.NoInternet -> {
                    Toast.makeText(
                        requireContext(),
                        "NoInternet",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        requireContext(),
                        "WHAAAT?",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.searchVacancyDetail(
            "061af1fd-e214-4cc9-bf26-300c62f06965"
        )
    }
}
