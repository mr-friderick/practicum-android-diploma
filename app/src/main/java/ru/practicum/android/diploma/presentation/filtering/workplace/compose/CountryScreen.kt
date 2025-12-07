package ru.practicum.android.diploma.presentation.filtering.workplace.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryScreen(
    onBackClick: () -> Unit,
    onAreaSelected: (Int, String) -> Unit = { _, _ -> }
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.country_select),
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
            val countryList = listOf(
                R.string.russia,
                R.string.ukraine,
                R.string.kazakhstan,
                R.string.azerbaijan,
                R.string.belarus,
                R.string.georgia,
                R.string.kyrgyzstan,
                R.string.uzbekistan,
                R.string.other_regions
            )
            LazyColumn {
                items(countryList) { resId ->
                    CountryItem(resId) {}
                }
            }
        }
    }
}

@Composable
fun CountryItem(
    text: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingBase, top = PaddingSmall, bottom = PaddingSmall, end = PaddingZero)
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(text),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            painter = painterResource(R.drawable.arrow_forward_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(PaddingBase)
        )
    }
}
