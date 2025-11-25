package ru.practicum.android.diploma.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val Typography = Typography(
    // Для заголовка экрана команд
    displayMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = FontSizeTextBig,
        lineHeight = FontLineHeightBig,
        letterSpacing = LetterSpacing
    ),
    // Для topBar,заголовков вакансий
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = FontSizeTextAverage,
        lineHeight = FontLineHeightAverage,
        letterSpacing = LetterSpacing
    ),
    // Обычный текст(описание,зп в вакансиях)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = FontSizeTextLittle,
        lineHeight = FontLineHeightLittle,
        letterSpacing = LetterSpacing
    )
)
