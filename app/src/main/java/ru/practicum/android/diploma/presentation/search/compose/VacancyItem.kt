package ru.practicum.android.diploma.presentation.search.compose

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.VacancyDetailModel
import ru.practicum.android.diploma.presentation.theme.ImageSize_48
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_1
import ru.practicum.android.diploma.presentation.theme.Padding_12
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.util.formatToSalary

@Composable
fun VacancyItem(
    vacancy: VacancyDetailModel,
    onClick: () -> Unit
) {
    val logoUrl = remember(vacancy.employer.logo) {
        vacancy.employer.logo.trim().takeIf { it.isNotBlank() }
    }

    val salaryText = remember(vacancy.salary) {
        if (vacancy.salary != null) {
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

    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(PaddingBase, PaddingSmall)
            .clickable(onClick = onClick)
    ) {
        Box(

            modifier = Modifier
                .padding(PaddingZero, PaddingZero, Padding_12, PaddingZero)
        ) {
            AsyncImage(
                model = logoUrl,
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
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = vacancy.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            if (salaryText != null) {
                Text(
                    text = salaryText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = Padding_4)
                )
            }
            Text(
                text = vacancy.employer.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = Padding_4)
            )
        }
    }
}
