package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldPrimaryLight,
    onPrimary = CreamSurface,
    primaryContainer = EmeraldPrimaryDark,
    onPrimaryContainer = EmeraldContainer,
    secondary = GoldAccent,
    onSecondary = DarkBackground,
    secondaryContainer = GoldDark,
    onSecondaryContainer = GoldLight,
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = EmeraldPrimary,
    onPrimary = CreamSurface,
    primaryContainer = EmeraldContainer,
    onPrimaryContainer = OnEmeraldContainer,
    secondary = GoldAccent,
    onSecondary = DarkBackground,
    secondaryContainer = GoldLight,
    onSecondaryContainer = GoldDark,
    background = CreamBackground,
    onBackground = TextPrimaryLight,
    surface = CreamSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = CreamBackground,
    onSurfaceVariant = TextSecondaryLight,
    outline = CreamBorder
)

@Composable
fun JannatAlRahmanTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if (darkTheme) DarkBackground.toArgb() else EmeraldPrimaryDark.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
