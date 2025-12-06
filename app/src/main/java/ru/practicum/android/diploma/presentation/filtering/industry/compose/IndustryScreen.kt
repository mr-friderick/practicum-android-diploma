package ru.practicum.android.diploma.presentation.filtering.industry.compose

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.Padding_24
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.presentation.theme.Size_20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustryScreen(
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.industry_find),
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
            SearchIndustry("") { print("f") } //изменить
            IndustryItem()
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.padding(horizontal = PaddingBase, Padding_24)) {
                Button(
                    onClick = { /* ... */ },
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
fun IndustryItem() {
    val listIndustry = listOf(
        "Авиаперевозки",
        "IT",
        "Автошкола",
        "Агрохимия (продвежение, оптовая торговля)",
        "Авиационная, вертолетная и аэрокосмическая промышленность",
        "Автокомпоненты, запчасти (производство)",
        "Автокомпоненты, запчасти, шины (продвеждение, оптовая торговля)",
        "Автомобильные перевозки",
        "Агентские услуги в недвижимости",
        "Агрохимия (продвежение, оптовая торговля)",
        "Агрохимия (производство)",
        "Алкогольные напитки (продвижение, оптовая торговля)1",
        "Автошкола1",
        "Агрохимия (продвежение, оптовая торговля)1"
    )
    var selectedIndustryId by remember { mutableStateOf<Int?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(listIndustry) { industry ->
            val index = listIndustry.indexOf(industry) // Получаем индекс текущего элемента

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedIndustryId = index }
            ) {
                Text(
                    text = industry,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = PaddingBase, vertical = Padding_4),
                    style = MaterialTheme.typography.bodyLarge
                )
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Blue,
                        unselectedColor = Blue
                    ),
                    selected = selectedIndustryId == index,
                    onClick = { selectedIndustryId = index }
                )
            }
        }
    }
}

@Composable
private fun SearchIndustry(
    searchText: String,
    onIndustryTextChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(FieldHeight)
            .padding(horizontal = PaddingBase, PaddingSmall)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(FieldHeight)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(PaddingSmall)
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { newText ->
                    onIndustryTextChanged(newText)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Black
                ),
                singleLine = true,
                cursorBrush = SolidColor(Blue),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = stringResource(R.string.industry_input),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        innerTextField()
                    }
                }
            )
            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = {
                        onIndustryTextChanged("")
                    },
                    modifier = Modifier.size(Size_20)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.close_24px),
                        contentDescription = stringResource(R.string.clear),
                        tint = Black,
                        modifier = Modifier.size(Size_20)
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.search_24px),
                    contentDescription = stringResource(R.string.search),
                    tint = Black,
                    modifier = Modifier.size(Size_20)
                )
            }
        }
    }
}
