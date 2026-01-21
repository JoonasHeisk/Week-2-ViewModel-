package com.example.viikkotehtava2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Teal80,
    secondary = Orange80,
    tertiary = Blue80,
    background = Gray80,
    surface = Gray40
)

private val LightColorScheme = lightColorScheme(
    primary = Teal40,
    secondary = Orange40,
    tertiary = Blue40,
    background = Gray40,
    surface = Gray80
)

@Composable
fun Viikkotehtava2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
