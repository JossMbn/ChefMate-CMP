package com.jmabilon.chefmate.core.designsystem.theme.extension

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun extendedColor(light: Color, dark: Color): Color {
    return if (isSystemInDarkTheme()) dark else light
}

val ColorScheme.shimmerInitialColor: Color @Composable get() = extendedColor(
    light = Color(0xFFE5D8C6),
    dark = Color(0xFF2D241A)
)

val ColorScheme.shimmerTargetColor: Color @Composable get() = extendedColor(
    light = Color(0xFFEAE2D4),
    dark = Color(0xFF3F2F21)
)
