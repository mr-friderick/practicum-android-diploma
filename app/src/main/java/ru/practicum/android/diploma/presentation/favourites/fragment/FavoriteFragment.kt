package ru.practicum.android.diploma.presentation.favourites.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.favourites.compose.FavoriteScreen
import ru.practicum.android.diploma.presentation.favourites.viewmodel.FavoriteViewModel
import ru.practicum.android.diploma.presentation.favourites.viewmodel.FavoriteViewState
import ru.practicum.android.diploma.presentation.theme.AppTheme

class FavoriteFragment : Fragment() {

    private val viewModel by viewModel<FavoriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getAll()

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@FavoriteFragment
                )
            )

            setContent {
                AppTheme {
                    val state by viewModel.state.observeAsState(FavoriteViewState.Loading)

                    FavoriteScreen(
                        state = state,
                        onVacancyClick = { vacancyId ->
                            findNavController().navigate(
                                R.id.vacancyDetailFragment,
                                bundleOf("vacancyId" to vacancyId)
                            )
                        }
                    )
                }
            }
        }
    }
}
