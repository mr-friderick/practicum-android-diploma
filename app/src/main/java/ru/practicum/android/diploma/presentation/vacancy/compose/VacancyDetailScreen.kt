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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VacancyDetailScreen(onBackClick: () -> Unit) {
    var isFavourite by remember { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.vacancy))
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.android_developer_2),
                style = MaterialTheme.typography.displayMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                stringResource(R.string._10000000000),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Column(
                Modifier
                    .padding(PaddingBase)
                    .verticalScroll(rememberScrollState())
            ) {
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
                            model = R.drawable.placeholder_32px,
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
                        Text(
                            text = stringResource(R.string.yandex),
                            style = MaterialTheme.typography.titleLarge,
                            color = Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(R.string.moscow),
                            style = MaterialTheme.typography.bodyLarge,
                            color = Black,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                VacancyTextContent()
            }

        }
    }
}

@Composable
fun VacancyTextContent() {
    // текст отформатирован, но так как не все данные будут в каждой вакансии
    // то функция нуждается в доработке
    Spacer(modifier = Modifier.height(Padding_24))
    InfoItem(R.string.required_experience, R.string.block_text)
    Text(
        stringResource(R.string.block_text),
        modifier = Modifier.padding(PaddingZero, PaddingSmall, PaddingZero, PaddingZero)
    )
    Spacer(modifier = Modifier.height(PaddingSmall))
    MiddleHeading(R.string.job_description)
    InfoItem(R.string.responsibilities, R.string.block_text)
    InfoItem(R.string.requirements, R.string.block_text)
    InfoItem(R.string.conditions, R.string.block_text)
    MiddleHeading(R.string.key_skills)
    InfoItem(R.string.conditions, R.string.block_text)
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

