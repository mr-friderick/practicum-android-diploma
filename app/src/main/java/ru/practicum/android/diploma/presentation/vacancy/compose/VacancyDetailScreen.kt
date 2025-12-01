package ru.practicum.android.diploma.presentation.vacancy.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_1
import ru.practicum.android.diploma.presentation.theme.Padding_12
import ru.practicum.android.diploma.presentation.theme.Padding_24
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.presentation.vacancy.viewmodel.VacancyDetailViewState
import ru.practicum.android.diploma.util.formatToSalary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailScreen(
    state: VacancyDetailViewState,
    onBackClick: () -> Unit
) {
    var isFavourite by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
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
                                onClick = {
                                    onBackClick()
                                },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                    )
                },
                actions = {
                    IconButton(onClick = { /**/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.sharing_24px),
                            contentDescription = stringResource(R.string.share),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = { isFavourite = !isFavourite }) {
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
    ) { paddingValues ->
        val vacancy = when (state) {
            is VacancyDetailViewState.VacancyDetail -> state.vacancyDetail
            else -> null
        }

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
            val salaryText = remember(vacancy?.salary) {
                if (vacancy?.salary != null) {
                    buildString {
                        vacancy.salary.from?.let { append("от ${it.formatToSalary()}") }
                        vacancy.salary.to?.let {
                            if (isNotEmpty()) {
                                append(" ")
                            }
                            append("до ${it.formatToSalary()}")
                        }
                        vacancy.salary.currency?.let { append(" $it") }
                    }
                } else {
                    null
                }
            }
            salaryText?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column(
                Modifier
                    .padding(PaddingBase)
                    .verticalScroll(rememberScrollState())
            ) {
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
                        vacancy?.address?.city?.let {
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
                VacancyTextContent(vacancy)
            }

        }
    }
}

@Composable
fun VacancyTextContent(vacancy: ru.practicum.android.diploma.domain.models.VacancyDetailModel?) {
    Spacer(modifier = Modifier.height(Padding_24))
    vacancy?.experience?.let { experience ->
        InfoItem(R.string.required_experience, experience.name)
    }
    Spacer(modifier = Modifier.height(PaddingSmall))
    MiddleHeading(R.string.job_description)
    vacancy?.description?.takeIf { it.isNotBlank() }?.let { description ->
        Text(
            description,
            modifier = Modifier.padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
        )
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
fun InfoItem(
    title: Int,
    contents: Int,
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
        text = stringResource(contents),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingZero, Padding_4, PaddingZero, PaddingZero)
    )
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

