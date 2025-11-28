package ru.practicum.android.diploma.presentation.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.search.compose.SearchScreen
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()

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
                        vacanciesPaging = viewModel.vacanciesPaging,
                        onSearchTextChange = { text ->
                            viewModel.updateSearchText(text)
                        },
                        onFavoriteClick = {
                            findNavController()
                                .navigate(R.id.action_searchFragment_to_favoriteFragment)
                        },
                        onDetailClick = { vacancyId ->
                            // TODO: передать ID вакансии в детальный экран
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
}
