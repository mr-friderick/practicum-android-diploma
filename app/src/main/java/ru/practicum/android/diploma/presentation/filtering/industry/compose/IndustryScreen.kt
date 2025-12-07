package ru.practicum.android.diploma.presentation.filtering.industry.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import ru.practicum.android.diploma.domain.models.FilterIndustryModel
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustrySelectViewModel
import ru.practicum.android.diploma.presentation.filtering.industry.viewmodel.IndustryViewState
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.Padding_24
import ru.practicum.android.diploma.presentation.theme.Padding_4
import ru.practicum.android.diploma.presentation.theme.Size_20
import ru.practicum.android.diploma.presentation.theme.Size_60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndustryScreen(
    viewModel: IndustrySelectViewModel,
    onBackClick: () -> Unit,
    onIndustrySelected: (FilterIndustryModel?) -> Unit
) {
    // Собираем состояния из ViewModel
    val filteredIndustries by viewModel.filteredIndustries.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val selectedIndustry by viewModel.selectedIndustry.collectAsState()

    // Получаем состояние загрузки из LiveData
    val state by viewModel.state.observeAsState()

    // Загружаем список отраслей при первом запуске
    LaunchedEffect(Unit) {
        viewModel.searchIndustries()
    }

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
            when (val currentState = state) {
                is IndustryViewState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.loading))
                    }
                }

                is IndustryViewState.Industry -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SearchIndustry(
                            searchText = searchText,
                            onIndustryTextChanged = { text ->
                                viewModel.updateSearchText(text)
                            }
                        )

                        IndustryItem(
                            industries = filteredIndustries,
                            selectedIndustry = selectedIndustry,
                            onIndustrySelected = { industry ->
                                viewModel.selectIndustry(industry)
                            }
                        )
                    }
                }

                is IndustryViewState.NoInternet -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(R.string.no_internet))
                    }
                }

                is IndustryViewState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ошибка: ${currentState.message}")
                    }
                }

                else -> {
                    // Пока ничего не загружено
                }
            }

            // Показываем кнопку выбора только если есть выбранная отрасль
            if (selectedIndustry != null) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = PaddingBase, Padding_24)
                ) {
                    Button(
                        onClick = {
                            // Передаем выбранную отрасль обратно
                            onIndustrySelected(selectedIndustry)
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
fun IndustryItem(
    industries: List<FilterIndustryModel>,
    selectedIndustry: FilterIndustryModel?,
    onIndustrySelected: (FilterIndustryModel?) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(industries) { industry ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Size_60)
                    .clickable {
                        // Если уже выбрана, снимаем выбор, иначе выбираем
                        if (selectedIndustry?.id == industry.id) {
                            onIndustrySelected(null)
                        } else {
                            onIndustrySelected(industry)
                        }
                    }
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
                    selected = selectedIndustry?.id == industry.id,
                    onClick = {
                        if (selectedIndustry?.id == industry.id) {
                            onIndustrySelected(null)
                        } else {
                            onIndustrySelected(industry)
                        }
                    }
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
