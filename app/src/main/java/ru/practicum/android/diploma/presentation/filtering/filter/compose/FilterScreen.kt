package ru.practicum.android.diploma.presentation.filtering.filter.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.models.FilterModel
import ru.practicum.android.diploma.presentation.filtering.filter.viewmodel.FilterViewModel
import ru.practicum.android.diploma.presentation.theme.Black
import ru.practicum.android.diploma.presentation.theme.Blue
import ru.practicum.android.diploma.presentation.theme.FieldHeight
import ru.practicum.android.diploma.presentation.theme.FontSizeText_12
import ru.practicum.android.diploma.presentation.theme.PaddingBase
import ru.practicum.android.diploma.presentation.theme.PaddingSmall
import ru.practicum.android.diploma.presentation.theme.PaddingZero
import ru.practicum.android.diploma.presentation.theme.Padding_24
import ru.practicum.android.diploma.presentation.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    viewModel: FilterViewModel,
    onBackClick: () -> Unit,
    onWorkPlaceClick: () -> Unit,
    onIndustryClick: () -> Unit,
) {
    val filterState by viewModel.filterState.observeAsState(FilterModel())
    val showResetButton by viewModel.showResetButton.observeAsState(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.filter_settings),
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
            // МЕСТО РАБОТЫ - показываем разный UI в зависимости от наличия выбора
            if (filterState.areaName != null) {
                // Если место работы выбрано - показываем с крестиком для сброса
                TextAndArrowOn(
                    text = R.string.place_of_work,
                    inputText = filterState.areaName!!,
                    onClick = { viewModel.updateWorkPlace(null) } // Сброс при клике на крестик
                )
            } else {
                // Если место работы НЕ выбрано - показываем со стрелкой для выбора
                TextAndArrowOff(
                    text = R.string.place_of_work,
                    onClick = { onWorkPlaceClick() }
                )
            }
            // ОТРАСЛЬ - показываем разный UI в зависимости от наличия выбора
            if (filterState.industryName != null) {
                // Если отрасль выбрана - показываем с крестиком для сброса
                TextAndArrowOn(
                    text = R.string.branch,
                    inputText = filterState.industryName!!,
                    onClick = { viewModel.updateIndustry(null) } // Сброс при клике на крестик
                )
            } else {
                // Если отрасль НЕ выбрана - показываем со стрелкой для выбора
                TextAndArrowOff(
                    text = R.string.branch,
                    onClick = { onIndustryClick() }
                )
            }
            Box(Modifier.padding(PaddingBase, Padding_24)) {
                Column() {
                    // Поле зарплаты - передаем текст из ViewModel и callback
                    InputField(
                        searchText = filterState.salary?.toString() ?: "",
                        onSearchTextChanged = { viewModel.updateSalary(it) }
                    )

                    // Чекбокс - передаем состояние из ViewModel
                    CheckboxSalary(
                        checked = filterState.onlyWithSalary ?: false,
                        onCheckedChange = { viewModel.updateHideWithoutSalary(it) }
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Кнопки - передаем флаг видимости кнопки "Сбросить"
                    Buttons(
                        showResetButton = showResetButton,
                        onApply = { /* задача №6 - сохранение фильтров */ },
                        onReset = { viewModel.resetFilters() }
                    )
                }
            }
        }
    }
}

@Composable
fun Buttons(
    showResetButton: Boolean,
    onApply: () -> Unit,
    onReset: () -> Unit
) {
    if (showResetButton) {
        Button(
            onClick = onApply,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .background(MaterialTheme.colorScheme.primary)
                .padding(horizontal = PaddingBase, PaddingSmall)
        ) {
            Text(
                stringResource(R.string.apply),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
        TextButton(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingBase, PaddingSmall)
        ) {
            Text(
                stringResource(R.string.throw_off),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun TextAndArrowOff(
    text: Int,
    onClick: () -> Unit // заменить тип получения
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
    text: Int, // ID ресурса заголовка (например, R.string.place_of_work)
    inputText: String, // Текст выбранного значения (не ID ресурса!)
    onClick: () -> Unit  // Callback для сброса
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingBase, top = PaddingSmall, bottom = PaddingSmall, end = PaddingZero),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
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

@Composable
private fun InputField(
    searchText: String,
    onSearchTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(FieldHeight)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(horizontal = PaddingBase, PaddingSmall),
    ) {
        Text(stringResource(R.string.expected_salary), fontSize = FontSizeText_12)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { newText ->
                    onSearchTextChanged(newText)
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodyLarge.copy(
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
                                text = stringResource(R.string.enter_the_amount),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        innerTextField()
                    }
                }
            )
            // КНОПКА ОЧИСТКИ (показывать только если есть текст)
            if (searchText.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.close_24px),
                    contentDescription = stringResource(R.string.clear),
                    modifier = Modifier
                        .clickable(
                            onClick = { onSearchTextChanged("") },
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun CheckboxSalary(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = PaddingZero, top = PaddingSmall, bottom = PaddingSmall, end = PaddingBase),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.do_not_show_without_salary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp) // Небольшой отступ от чекбокса
        )
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = true,
            colors = CheckboxDefaults.colors(
                checkedColor = Blue,
                uncheckedColor = Blue,
                checkmarkColor = White
            )
        )
    }
}
