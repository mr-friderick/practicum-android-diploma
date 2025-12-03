package ru.practicum.android.diploma.presentation.vacancy.compose

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_1
import ru.practicum.android.diploma.presentation.theme.Padding_12
import ru.practicum.android.diploma.presentation.theme.Padding_24
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewModel
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewState
import ru.practicum.android.diploma.util.formatToSalary

@Composable
fun VacancyDetailScreen(
    viewModel: VacancyDetailViewModel,
    state: VacancyDetailViewState,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String, String?) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            when (state) {
                is VacancyDetailViewState.VacancyDetail -> {
                    VacancyDetailTopBar(
                        isFavourite = state.isFavorite,
                        onFavouriteClick = { viewModel.favoriteControl() },
                        onBackClick = onBackClick,
                        onShareClick = onShareClick
                    )
                }
                else -> {
                    VacancyDetailTopBar(
                        isFavourite = false,
                        onFavouriteClick = { },
                        onBackClick = onBackClick,
                        onShareClick = onShareClick
                    )
                }
            }
        }
    ) { paddingValues ->
        when (state) {
            is VacancyDetailViewState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is VacancyDetailViewState.VacancyDetail -> {
                VacancyDetailContent(
                    vacancy = state.vacancyDetail,
                    paddingValues = paddingValues,
                    onEmailClick = onEmailClick,
                    onPhoneClick = onPhoneClick
                )
            }

            is VacancyDetailViewState.NotFound -> {
                DisplayPH(
                    R.drawable.ph_not_found,
                    R.string.not_found_facancy
                )
            }

            is VacancyDetailViewState.NoInternet -> {
                DisplayPH(
                    R.drawable.skull,
                    R.string.no_internet
                )
            }

            is VacancyDetailViewState.Error -> {
                DisplayPH(
                    R.drawable.ph_server_error_2,
                    R.string.server_error
                )
            }
        }
    }
}

@Composable
fun DisplayPH(image: Int, text: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .size(width = 328.dp, height = 223.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Text(
            modifier = Modifier
                .padding(top = PaddingBase)
                .fillMaxWidth(),
            text = stringResource(text),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VacancyDetailTopBar(
    isFavourite: Boolean,
    onFavouriteClick: () -> Unit,
    onBackClick: () -> Unit,
    onShareClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.vacancy),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.arrow_back_24px),
                contentDescription = null,
                modifier = Modifier
                    .padding(PaddingBase)
                    .clickable(
                        onClick = onBackClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
            )
        },
        actions = {
            IconButton(onClick = onShareClick) {
                Icon(
                    painter = painterResource(id = R.drawable.sharing_24px),
                    contentDescription = stringResource(R.string.share),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onFavouriteClick) {
                Icon(
                    painter = painterResource(
                        id = if (isFavourite) {
                            R.drawable.favorites_on__24px
                        } else {
                            R.drawable.favorites_off__24px
                        }
                    ),
                    contentDescription = stringResource(R.string.favourites),
                    tint = if (isFavourite) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    )
}

@Composable
private fun VacancyDetailContent(
    vacancy: VacancyDetailModel?,
    paddingValues: androidx.compose.foundation.layout.PaddingValues,
    onEmailClick: (String) -> Unit,
    onPhoneClick: (String, String?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        vacancy?.name?.let {
            Text(
                it,
                style = MaterialTheme.typography.displayMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(horizontal = PaddingBase)
            )
        }
        val salaryText = formatSalaryText(vacancy?.salary)
        if (salaryText != null) {
            Text(
                salaryText,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingBase)
            )
        } else if (vacancy?.salary != null) {
            Text(
                stringResource(R.string.salary_not_specified),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingBase)
            )
        }
        Column(
            Modifier
                .padding(PaddingBase)
                .verticalScroll(rememberScrollState())
        ) {
            EmployerInfoCard(vacancy)
            VacancyTextContent(
                vacancy = vacancy,
                onEmailClick = onEmailClick,
                onPhoneClick = onPhoneClick
            )
        }
    }
}

@Composable
private fun formatSalaryText(salary: ru.practicum.android.diploma.domain.models.SalaryModel?): String? {
    return remember(salary) {
        if (salary != null) {
            val hasFrom = salary.from != null
            val hasTo = salary.to != null
            if (hasFrom || hasTo) {
                buildString {
                    salary.from?.let { append("от ${it.formatToSalary()}") }
                    salary.to?.let {
                        if (isNotEmpty()) {
                            append(" ")
                        }
                        append("до ${it.formatToSalary()}")
                    }
                    salary.currency?.let { append(" $it") }
                }
            } else {
                null
            }
        } else {
            null
        }
    }
}

@Composable
private fun EmployerInfoCard(vacancy: VacancyDetailModel?) {
    val logoUrl = remember(vacancy?.employer?.logo) {
        vacancy?.employer?.logo?.trim()?.takeIf { it.isNotBlank() }
    }
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.outline)
            .padding(PaddingBase)
    ) {
        Box(
            modifier = Modifier
                .padding(PaddingZero, PaddingZero, Padding_12, PaddingZero)
        ) {
            AsyncImage(
                model = logoUrl ?: R.drawable.placeholder_32px,
                contentDescription = stringResource(R.string.job_cover),
                placeholder = painterResource(id = R.drawable.placeholder_32px),
                error = painterResource(id = R.drawable.placeholder_32px),
                modifier = Modifier
                    .size(ImageSize_48)
                    .clip(MaterialTheme.shapes.large)
                    .border(
                        width = Padding_1,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.large
                    ),
                contentScale = ContentScale.Fit,
                onError = { result ->
                    Log.e(
                        "VacancyItem",
                        "AsyncImage error: ${result.result.throwable.message}",
                        result.result.throwable
                    )
                },
                onSuccess = {
                    Log.d("VacancyItem", "AsyncImage loaded successfully")
                }
            )
        }
        Column(modifier = Modifier.fillMaxWidth()) {
            vacancy?.employer?.name?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.titleLarge,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            val addressText = vacancy?.address?.raw ?: vacancy?.address?.city
            addressText?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

@Composable
fun VacancyTextContent(
    vacancy: VacancyDetailModel?,
    onEmailClick: (String) -> Unit = {},
    onPhoneClick: (String, String?) -> Unit = { _, _ -> }
) {
    Spacer(modifier = Modifier.height(Padding_24))
    vacancy?.experience?.let { experience ->
        InfoItem(R.string.required_experience, experience.name)
    }
    vacancy?.employment?.let { employment ->
        Text(
            text = employment.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
        )
    }
    Spacer(modifier = Modifier.height(PaddingSmall))
    MiddleHeading(R.string.job_description)
    vacancy?.description?.takeIf { it.isNotBlank() }?.let { description ->
        Text(
            description,
            modifier = Modifier.padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
        )
    }
    MiddleHeading(R.string.contacts)
    vacancy?.contacts?.let { contacts ->
        ContactItem(contacts.name)

        if (contacts.email.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .clickable(onClick = { onEmailClick(contacts.email) })
            ) {
                Text(
                    text = contacts.email,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = PaddingSmall)
                )
            }
            ContactItem(contacts.email)
        }
        if (contacts.phones.isNotEmpty()) {
            Text(
                text = stringResource(R.string.phones),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = PaddingSmall)
            )

            contacts.phones.forEachIndexed { index, phone ->
                Box(
                    modifier = Modifier
                        .clickable(onClick = { onPhoneClick(phone.formatted, phone.comment) })
                ) {
                    Text(
                        text = "${index + 1}. ${phone.formatted}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = PaddingSmall)
                    )
                    if (!phone.comment.isNullOrEmpty()) {
                        Text(
                            text = phone.comment,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = PaddingSmall)
                        )
                    }
                }
            }
        }
    }
    MiddleHeading(R.string.key_skills)
    vacancy?.skills?.takeIf { it.isNotEmpty() }?.forEach { skill ->
        Text(
            "• $skill",
            modifier = Modifier.padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
        )
    }
}

@Composable
fun MiddleHeading(text: Int) {
    Text(
        text = stringResource(text),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingZero, Padding_24, PaddingZero, PaddingZero),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ContactItem(content: String) {
    Box(
        modifier = Modifier
            .clickable(onClick = {
                // Обработка клика
            })
    ) {
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
        )
    }
}

@Composable
fun InfoItem(
    title: Int,
    content: String,
) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingZero, PaddingBase, PaddingZero, PaddingZero),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        text = content,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
    )
}

