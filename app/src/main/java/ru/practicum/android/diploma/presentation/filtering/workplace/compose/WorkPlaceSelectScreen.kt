package ru.practicum.android.diploma.presentation.filtering.workplace.compose
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.FontSizeText_12
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_24

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkPlaceSelectScreen(
    onBackClick: () -> Unit,
    onCountryClick: () -> Unit,
    onRegionClick: () -> Unit,
    selectedCountry: String? = null,
    selectedRegion: String? = null,
    onCountryClear: () -> Unit = {},
    onRegionClear: () -> Unit = {},
    onApplyClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.workplace_find),
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Поле для страны
            if (selectedCountry != null) {
                TextAndArrowOn(
                    text = R.string.country,
                    inputText = selectedCountry,
                    onClick = {
                        // Крестик - очищаем страну
                        onCountryClear()
                    },
                    onClickScoreboard = {
                        // Нажатие на текст - редактируем страну
                        onCountryClick()
                    }
                )
            } else {
                TextAndArrowOff(
                    text = R.string.country,
                    onClick = { onCountryClick() }
                )
            }

            // Поле для региона
            if (selectedRegion != null) {
                TextAndArrowOn(
                    text = R.string.region,
                    inputText = selectedRegion,
                    onClick = {
                        // Крестик - очищаем регион
                        onRegionClear()
                    },
                    onClickScoreboard = {
                        // Нажатие на текст - редактируем регион
                        onRegionClick()
                    }
                )
            } else {
                TextAndArrowOff(
                    text = R.string.region,
                    onClick = { onRegionClick() }
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Box(Modifier.padding(PaddingBase, Padding_24)) {
                Button(
                    onClick = onApplyClick,  // ДОБАВЬ onApplyClick
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = PaddingBase, PaddingSmall)
                ) {
                    Text(
                        stringResource(R.string.choose),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Composable
fun TextAndArrowOff(
    text: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingBase, top = PaddingSmall, bottom = PaddingSmall, end = PaddingZero),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(text),
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.tertiary
        )
        Icon(
            painter = painterResource(R.drawable.arrow_forward_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(PaddingBase)
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )
    }
}

@Composable
fun TextAndArrowOn(
    text: Int,
    inputText: String,
    onClick: () -> Unit,
    onClickScoreboard: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingBase, top = PaddingSmall, bottom = PaddingSmall, end = PaddingZero),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            Modifier
                .weight(1f)
                .clickable(
                    onClick = onClickScoreboard,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
        ) {
            Text(
                stringResource(text),
                fontSize = FontSizeText_12
            )
            Text(
                inputText,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Icon(
            painter = painterResource(R.drawable.close_24px),
            contentDescription = null,
            modifier = Modifier
                .padding(PaddingBase)
                .clickable(
                    onClick = onClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )
    }
}
