package com.duoc.evpanalsuitemobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF092EB4),
    onPrimary = Color.White,
    secondary = Color(0xFF6B4F1D)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF0037FF),
    onPrimary = Color(0xFF3B2A00),
    secondary = Color(0xFFD9B46A)
)

@Composable
fun PanalSuiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}