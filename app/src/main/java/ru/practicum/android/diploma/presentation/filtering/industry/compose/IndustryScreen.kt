package ru.practicum.android.diploma.presentation.filtering.industry.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustryViewState
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.Padding_24
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.presentation.theme.Size_20
import ru.practicum.android.diploma.presentation.theme.Size_60
import ru.practicum.android.diploma.presentation.vacancy.compose.DisplayPH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustryScreen(
    viewState: IndustryViewState,
    initialSelectedIndustryId: Int? = null,
    onBackClick: () -> Unit,
    onIndustrySelected: (Int, String) -> Unit = { _, _ -> }
) {
    var searchText by remember { mutableStateOf("") }
    var selectedIndustryId by remember(initialSelectedIndustryId) {
        mutableStateOf<Int?>(initialSelectedIndustryId)
    }

    // Получаем исходный список отраслей
    val allIndustries = when (viewState) {
        is IndustryViewState.Industry -> viewState.industries
        else -> emptyList()
    }

    // Фильтрация списка отраслей по тексту поиска
    val filteredIndustries = if (searchText.isBlank()) {
        allIndustries
    } else {
        allIndustries.filter { industry ->
            industry.name.contains(searchText, ignoreCase = true)
        }
    }

    // Находим выбранную отрасль в исходном списке
    val selectedIndustry = allIndustries.find { it.id == selectedIndustryId }
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SearchIndustry(searchText) { newText ->
                    searchText = newText
                }
                IndustryContent(
                    viewState = viewState,
                    industries = filteredIndustries,
                    selectedIndustryId = selectedIndustryId,
                    hasSelectedIndustry = selectedIndustry != null,
                    onIndustryClick = { industryId ->
                        selectedIndustryId = industryId
                    }
                )
            }
            // Показывать кнопку только при выбранном элементе
            if (selectedIndustry != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = PaddingBase, Padding_24)
                ) {
                    Button(
                        onClick = {
                            onIndustrySelected(selectedIndustry.id, selectedIndustry.name)
                        },
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
}

@Composable
fun IndustryContent(
    viewState: IndustryViewState,
    industries: List<FilterIndustryModel>,
    selectedIndustryId: Int?,
    hasSelectedIndustry: Boolean,
    onIndustryClick: (Int) -> Unit
) {
    when (viewState) {
        is IndustryViewState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        is IndustryViewState.NoInternet -> {
            DisplayPH(R.drawable.skull, R.string.no_internet)
        }

        is IndustryViewState.Error -> {
           DisplayPH(R.drawable.carpet, R.string.couldnt_get_the_list)
        }

        is IndustryViewState.Industry -> {
            if (industries.isEmpty()) {
                DisplayPH(R.drawable.cat, R.string.there_no_industry)
            } else {
                IndustryItem(
                    industries = industries,
                    selectedIndustryId = selectedIndustryId,
                    hasSelectedIndustry = hasSelectedIndustry,
                    onIndustryClick = onIndustryClick
                )
            }
        }
    }
}

@Composable
fun IndustryItem(
    industries: List<FilterIndustryModel>,
    selectedIndustryId: Int?,
    hasSelectedIndustry: Boolean,
    onIndustryClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = if (hasSelectedIndustry) {
            PaddingValues(bottom = 100.dp)
        } else {
            PaddingValues()
        }
    ) {
        items(industries) { industry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size_60)
                    .clickable { onIndustryClick(industry.id) }
            ) {
                Text(
                    text = industry.name,
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
                    selected = selectedIndustryId == industry.id,
                    onClick = { onIndustryClick(industry.id) }
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
}
