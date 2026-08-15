package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = VioletPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0x1A6C5CE7),
    onPrimaryContainer = VioletDim,
    secondary = CyanAccent,
    onSecondary = Color(0xFF00363D),
    secondaryContainer = Color(0x1A22D3EE),
    onSecondaryContainer = Color(0xFF004F58),
    tertiary = PinkAccent,
    background = BgColor,
    onBackground = TextColor,
    surface = SurfaceSolid,
    onSurface = TextColor,
    surfaceVariant = Color(0xFFF2F4FC),
    onSurfaceVariant = TextMuted,
    outline = BorderLine,
    error = DangerColor,
    onError = Color.White
)

@Composable
fun SchoolPOSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
