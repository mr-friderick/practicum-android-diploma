package ru.practicum.android.diploma.presentation.search.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.search.compose.SearchScreen
import ru.practicum.android.diploma.presentation.search.viewmodel.SearchViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class SearchFragment : Fragment() {

    private val viewModel by viewModel<SearchViewModel>()
    private val filterViewModel: FilterViewModel by viewModel(ownerProducer = { requireActivity() })

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
                    val filterState by filterViewModel.filterState.collectAsState()
                    val filterApplied by filterViewModel.filterApplied.collectAsState()

                    LaunchedEffect(filterApplied) {
                        if (filterApplied) {
                            val searchText = viewModel.getSearchText()
                            if (searchText.isNotBlank()) {
                                viewModel.searchVacancy(searchText, filterState)
                            }
                            filterViewModel.clearFilterAppliedFlag()
                        }
                    }

                    SearchScreen(
                        viewModel = viewModel,
                        hasActiveFilters = filterState?.let { hasActiveFilters(it) } ?: false,
                        onSearchTextChange = { text ->
                            viewModel.searchVacancy(text, filterState)
                        },
                        onFilterFragment = {
                            findNavController()
                                .navigate(R.id.action_searchFragment_to_filterFragment)
                        },
                        onDetailClick = { vacancyId ->
                            findNavController()
                                .navigate(
                                    R.id.action_searchFragment_to_vacancyDetailFragment,
                                    Bundle().apply {
                                        putString("vacancyId", vacancyId)
                                    }
                                )
                        }
                    )
                }
            }
        }
    }

    private fun hasActiveFilters(filter: FilterModel): Boolean {
        return filter.areaId != null ||
            filter.industryId != null ||
            filter.salary != null ||
            filter.onlyWithSalary == true
    }
}

