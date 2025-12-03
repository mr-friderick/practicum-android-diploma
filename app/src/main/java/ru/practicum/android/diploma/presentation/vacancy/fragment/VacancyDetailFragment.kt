package ru.practicum.android.diploma.presentation.vacancy.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.AppTheme
import ru.practicum.android.diploma.presentation.vacancy.compose.VacancyDetailScreen
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewState

class VacancyDetailFragment : Fragment() {

    private val viewModel by viewModel<VacancyDetailViewModel>()

    companion object {
        private const val TEXT_PLAIN_TYPE = "text/plain"
        private const val TAG = "VacancyDetailFragment"
    }

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
                    val contactIntent by viewModel.contactIntent.observeAsState()

                    // Обработка контактных интентов
                    LaunchedEffect(contactIntent) {
                        contactIntent?.let { intent ->
                            when (intent) {
                                is VacancyDetailViewModel.ContactIntent.Email -> {
                                    openEmailClient(intent.email)
                                    viewModel.onContactIntentHandled()
                                }
                                is VacancyDetailViewModel.ContactIntent.Phone -> {
                                    openPhoneDialer(intent.phoneNumber, intent.comment)
                                    viewModel.onContactIntentHandled()
                                }
                            }
                        }
                    }


                    VacancyDetailScreen(
                        viewModel = viewModel,
                        state = state ?: VacancyDetailViewState.Loading,
                        onBackClick = {
                            findNavController().popBackStack()
                        },
                        onShareClick = {
                            viewModel.getShareUrl()?.let { url ->
                                showStandardShareDialog(url)
                            }
                        },
                        onEmailClick = { email ->
                            viewModel.onEmailClicked(email)
                        },
                        onPhoneClick = { phone, comment ->
                            viewModel.onPhoneClicked(phone, comment)
                        }
                    )
                }
            }
        }
    }
    private fun showStandardShareDialog(url: String) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = TEXT_PLAIN_TYPE
        }

        val shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_vacancy))

        try {
            startActivity(shareIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "No activity found to handle share intent", e)
        }
    }

    private fun openEmailClient(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }

        val chooser = Intent.createChooser(intent, getString(R.string.mail))

        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "No email app found", e)
        }
    }

    private fun openPhoneDialer(phoneNumber: String, comment: String?) {
        val cleanPhone = phoneNumber.replace(Regex("[^+\\d]"), "")
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$cleanPhone")
        }

        if (!comment.isNullOrBlank()) {
            intent.putExtra("android.intent.extra.SUBJECT", comment)
        }

        val chooser = Intent.createChooser(intent, getString(R.string.phones))

        try {
            startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.e(TAG, "No phone app found", e)
        }
    }
}
