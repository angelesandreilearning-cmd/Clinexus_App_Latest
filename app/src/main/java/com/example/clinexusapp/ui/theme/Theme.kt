package com.example.clinexusapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueDark,
    onPrimaryContainer = BlueLight,
    secondary = PeachPrimary,
    onSecondary = Black,
    secondaryContainer = Color(0xFF2C3E50), // Darker Peach-complimentary
    onSecondaryContainer = PeachLight,
    background = Black, // rich dark slate from Color.kt
    surface = Color(0xFF1E293B), // Modern dark surface
    onBackground = White,
    onSurface = White,
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1)
)

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueExtraLight,
    onPrimaryContainer = BlueDark,
    secondary = PeachPrimary,
    onSecondary = Black,
    secondaryContainer = PeachLight,
    onSecondaryContainer = PeachDark,
    background = GrayLight,
    surface = White,
    onBackground = Black,
    onSurface = Black,
    surfaceVariant = GrayMedium,
    onSurfaceVariant = GrayDark
)

@Composable
fun ClinexusAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
