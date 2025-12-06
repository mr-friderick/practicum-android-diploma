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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.R
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
    onBackClick: () -> Unit,
    onWorkPlaceClick: () -> Unit,
    onIndustryClick: () -> Unit,
) {
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
            // заменить функции которые будут применяться при клике на иконку
            TextAndArrowOff(R.string.place_of_work) { onWorkPlaceClick() }
            // заменить функции которые будут применяться при клике на иконку и сам текст из вью модели
            TextAndArrowOn(R.string.branch, R.string.there_will_be_a_text_here) { onIndustryClick() }
            // заменить функцию которая будет применяться при вводе и сам текст из вью модели
            Box(Modifier.padding(PaddingBase, Padding_24)) {
                Column() {
                    InputField("чччччч" ) { onWorkPlaceClick() }
                    CheckboxSalary()
                    Spacer(modifier = Modifier.weight(1f))
                    Buttons() }

            }


        }
    }
}

@Composable
fun Buttons() {
    Button(
        onClick = { /* ... */ },
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
        onClick = { /* ... */ },
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

@Composable
fun TextAndArrowOff(
    text: Int,
    onClick: () -> Unit // заменить тип получения
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(PaddingBase, PaddingSmall),
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
    inputText: Int,
    onClick: () -> Unit // заменить тип получения
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(PaddingBase, PaddingSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                stringResource(text),
                fontSize = FontSizeText_12
            )
            Text(
                stringResource(inputText),
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
    }
}

@Preview
@Composable
fun CheckboxSalary() {
    var checkedState by remember { mutableStateOf(false) }
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
            checked = checkedState,
            onCheckedChange = { checkedState = it },
            enabled = true,
            colors = CheckboxDefaults.colors(
                checkedColor = Blue,
                uncheckedColor = Blue,
                checkmarkColor = White
            )
        )
    }
}
