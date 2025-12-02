package ru.practicum.android.diploma.presentation.vacancy.fragment

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ActivityNotFoundException
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.presentation.theme.AppTheme
import ru.practicum.android.diploma.presentation.vacancy.compose.VacancyDetailScreen
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewState
import ru.practicum.android.diploma.util.ShareTarget
import ru.practicum.android.diploma.util.queryShareTargets
import android.util.Log

class VacancyDetailFragment : Fragment() {

    private val viewModel by viewModel<VacancyDetailViewModel>()
    private var shareTargets: List<ShareTarget> = emptyList()

    companion object {
        private const val VACANCY_ID_ARG = "vacancyId"
        private const val TEXT_PLAIN_TYPE = "text/plain"
        private const val TAG = "VacancyDetailFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val vacancyId = arguments?.getString(VACANCY_ID_ARG)

        if (vacancyId != null) {
            viewModel.searchVacancyDetail(vacancyId)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val targets = queryShareTargets(requireContext())
            withContext(Dispatchers.Main) {
                shareTargets = targets
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@VacancyDetailFragment
                )
            )

            setContent {
                AppTheme {
                    val state by viewModel.state.observeAsState(VacancyDetailViewState.Loading)
                    val showShareSheet by viewModel.showShareSheet.observeAsState(false)
                    val isFav by viewModel.isFavorite.observeAsState(false)

                    VacancyDetailScreen(
                        state = state,
                        onBackClick = { findNavController().popBackStack() },
                        showShareSheet = showShareSheet,
                        onShareSheetDismiss = { viewModel.onShareSheetDismissed() },
                        onShowShareSheet = { viewModel.onShareClicked() },
                        shareTargets = shareTargets,
                        onShareTargetSelected = { target ->
                            shareVacancy(viewModel.getShareUrl(), target)
                        },
                        isFavourite = isFav,
                        onFavouriteClick = { viewModel.toggleFavorite() },
                        onMoreClicked = {
                            val url = viewModel.getShareUrl()
                            url?.let { vacancyUrl ->
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, vacancyUrl)
                                    type = TEXT_PLAIN_TYPE
                                }
                                val chooser = Intent.createChooser(sendIntent, null)
                                if (chooser.resolveActivity(requireContext().packageManager) != null) {
                                    startActivity(chooser)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    private fun shareVacancy(url: String?, target: ShareTarget) {
        url?.let { vacancyUrl ->
            val pm = requireContext().packageManager
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, vacancyUrl)
                type = TEXT_PLAIN_TYPE
                component = ComponentName(target.packageName, target.activityName)
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // fallback: если явно указанный component не сработал, используем chooser
                val chooser = Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, vacancyUrl)
                    type = TEXT_PLAIN_TYPE
                }, null)
                if (chooser.resolveActivity(pm) != null) {
                    startActivity(chooser)
                }
            } catch (e: SecurityException) {
                Log.e(TAG, "Security exception when trying to share", e)
                // Показываем toast или другой UI feedback
                showFallbackShare(vacancyUrl)
            }
        }
    }

    private fun showFallbackShare(vacancyUrl: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, vacancyUrl)
            type = TEXT_PLAIN_TYPE
        }
        val chooser = Intent.createChooser(sendIntent, null)
        if (chooser.resolveActivity(requireContext().packageManager) != null) {
            startActivity(chooser)
        }
    }

    fun startShareToTarget(context: Context, target: ShareTarget, text: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = TEXT_PLAIN_TYPE
            putExtra(Intent.EXTRA_TEXT, text)
            component = ComponentName(target.packageName, target.activityName)
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // если запустить по component не получилось — открываем chooser как fallback
            val chooser = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = TEXT_PLAIN_TYPE
                putExtra(Intent.EXTRA_TEXT, text)
            }, null)
            context.startActivity(chooser)
        } catch (e: SecurityException) {
            Log.e(TAG, "Security exception when trying to share", e)
            val chooser = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                type = TEXT_PLAIN_TYPE
                putExtra(Intent.EXTRA_TEXT, text)
            }, null)
            context.startActivity(chooser)
        }
    }
}
