package ru.practicum.android.diploma.presentation.filtering.workplace.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.filtering.workplace.compose.CountryScreen
import ru.practicum.android.diploma.presentation.filtering.workplace.viewmodel.CountrySelectViewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme

class CountrySelectFragment : Fragment() {

    private val viewModel by viewModel<CountrySelectViewModel>()
    private val filterViewModel: FilterViewModel by viewModel(ownerProducer = { requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@CountrySelectFragment
                )
            )

            setContent {
                AppTheme {
                    val state = viewModel.state.observeAsState()

                    CountryScreen(
                        onBackClick = { findNavController().popBackStack() },
                        onAreaSelected = { areaId, areaName ->
                            filterViewModel.updateCountry(areaId, areaName)
                            findNavController().popBackStack()
                        },
                        countryState = state.value
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCountries()
    }

    private fun loadCountries() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchCountries()
        }
    }
}
