package ru.practicum.android.diploma.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val primaryColor = Blue
    val onPrimaryColor = White
    val backgroundColor = if (darkTheme) {
        Black
    } else {
        White
    }
    val textColor = if (darkTheme) {
        White
    } else {
        Black
    }
    val backgroundEditText = if (darkTheme) {
        Gray
    } else {
        LightGray
    }

    val hintEditText = if (darkTheme) {
        White
    } else {
        Gray
    }

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = onPrimaryColor,
            background = backgroundColor,
            onBackground = textColor,
            surface = backgroundColor,
            onSurface = textColor,
            tertiaryContainer = backgroundEditText,
            onTertiaryContainer = hintEditText,
            outline = LightGray,
            error = Red,
            tertiary = Gray
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = onPrimaryColor,
            background = backgroundColor,
            onBackground = textColor,
            surface = backgroundColor,
            onSurface = textColor,
            tertiaryContainer = backgroundEditText,
            onTertiaryContainer = hintEditText,
            outline = LightGray,
            error = Red,
            tertiary = Gray
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
