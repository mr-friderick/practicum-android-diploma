package ru.practicum.android.diploma.presentation.search.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_12

@Preview
@Composable
fun VacancyItem() {
    Row(modifier = Modifier.background(MaterialTheme.colorScheme.background)
        .padding(PaddingBase,PaddingSmall)
    ) {
        Box(
            modifier = Modifier
                .padding(PaddingZero,PaddingZero, Padding_12,PaddingZero)
        ) {
            AsyncImage(
                model = R.drawable.placeholder_32px,//заменить на источник изображения вакансии
                contentDescription = stringResource(R.string.job_cover),
                placeholder = painterResource(id = R.drawable.placeholder_32px),
                error = painterResource(id = R.drawable.placeholder_32px),
                modifier = Modifier
                    .size(ImageSize_48)
                    .clip(MaterialTheme.shapes.large)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = MaterialTheme.shapes.large
                    ),
                contentScale = ContentScale.Crop
            )
        }
        Column() {
            Text(
                stringResource(R.string.android_developer),
                style = MaterialTheme.typography.titleLarge
            )//заменить текст на источник названия вакансии
            Text(
                stringResource(R.string.yandex),
                style = MaterialTheme.typography.bodyLarge
            )//заменить текст на источник автора вакансии
            Text(
                stringResource(R.string._10000000000),
                style = MaterialTheme.typography.bodyLarge
            )//заменить текст на источник зарплаты вакансии
        }
    }
}
