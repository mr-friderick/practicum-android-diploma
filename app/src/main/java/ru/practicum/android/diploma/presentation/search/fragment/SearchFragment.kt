package ru.practicum.android.diploma.presentation.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.search.compose.SearchScreen
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewState
import ru.practicum.android.diploma.presentation.theme.AppTheme

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@SearchFragment
                )
            )
            setContent {
                AppTheme {
                    SearchScreen(
                        onFavoriteClick = {
                            findNavController()
                                .navigate(R.id.action_searchFragment_to_favoriteFragment)
                        },
                        onTeamClick = {
                            findNavController()
                                .navigate(R.id.action_searchFragment_to_teamFragment)
                        },
                        onDetailClick = {
                            findNavController()
                                .navigate(R.id.action_searchFragment_to_vacancyDetailFragment)
                        },
                        onFilterFragment = {
                            findNavController()
                                .navigate(R.id.action_searchFragment_to_filterFragment)
                        }
                    )
                }
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchViewState.Default -> {
                    Toast.makeText(
                        requireContext(),
                        "Default",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is SearchViewState.NotFound -> {
                    Toast.makeText(
                        requireContext(),
                        "NotFound",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is SearchViewState.Vacancy -> {
                    Toast.makeText(
                        requireContext(),
                        "Vacancy",
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
    }
}
