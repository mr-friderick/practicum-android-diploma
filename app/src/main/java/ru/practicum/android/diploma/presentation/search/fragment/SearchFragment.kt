package ru.practicum.android.diploma.presentation.search.fragment

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
import ru.practicum.android.diploma.presentation.search.compose.SearchScreen
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {

            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@SearchFragment
                )
            )

            setContent {
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
