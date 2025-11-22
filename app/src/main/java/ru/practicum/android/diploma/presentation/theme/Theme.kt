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

    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = primaryColor,
            onPrimary = onPrimaryColor,
            background = backgroundColor,
            onBackground = textColor,
            surface = backgroundColor,
            onSurface = textColor,
        )
    } else {
        lightColorScheme(
            primary = primaryColor,
            onPrimary = onPrimaryColor,
            background = backgroundColor,
            onBackground = textColor,
            surface = backgroundColor,
            onSurface = textColor,
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = AppShapes,
        content = content
    )
}
