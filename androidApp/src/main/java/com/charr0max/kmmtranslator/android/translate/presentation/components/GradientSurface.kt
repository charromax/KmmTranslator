package com.charr0max.kmmtranslator.android.translate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * custom modifier for applying gradient in dark theme
 * use composed keyword if the modifier function is stateful
 */
fun Modifier.gradientSurface(): Modifier = composed {
    if (isSystemInDarkTheme()) {
        Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF23262E),
                    Color(0xFF212329),
                )
            )
        )
    } else {
        Modifier.background(MaterialTheme.colors.surface)
    }
}