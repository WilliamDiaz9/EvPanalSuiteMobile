package com.duoc.evpanalsuitemobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes

private val LightColors = lightColorScheme(
    primary = Color(0xFF9A5B13),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE2B7),
    onPrimaryContainer = Color(0xFF321A00),
    secondary = Color(0xFF41634F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD9EBDD),
    onSecondaryContainer = Color(0xFF102719),
    tertiary = Color(0xFF745B35),
    background = Color(0xFFFAF7F1),
    surface = Color(0xFFFFFDF9),
    surfaceVariant = Color(0xFFF1EADF),
    onSurface = Color(0xFF29271F),
    onSurfaceVariant = Color(0xFF625E54),
    outline = Color(0xFF81796B)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFFFD08B),
    onPrimary = Color(0xFF492900),
    primaryContainer = Color(0xFF70430A),
    onPrimaryContainer = Color(0xFFFFE2B7),
    secondary = Color(0xFFB7D2BD),
    onSecondary = Color(0xFF223A2A),
    secondaryContainer = Color(0xFF354D3C),
    tertiary = Color(0xFFE5C99B),
    background = Color(0xFF171912),
    surface = Color(0xFF1F211A),
    surfaceVariant = Color(0xFF34362E),
    onSurface = Color(0xFFE7E3D8),
    onSurfaceVariant = Color(0xFFC9C4B8),
    outline = Color(0xFF938E82)
)

private val PanalShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun PanalSuiteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        shapes = PanalShapes,
        content = content
    )
}

