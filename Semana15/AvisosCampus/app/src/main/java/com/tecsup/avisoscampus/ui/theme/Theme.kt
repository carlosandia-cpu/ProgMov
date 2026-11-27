package com.tecsup.avisoscampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresAvisos = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    secondary = Color(0xFF546E7A),
    background = Color(0xFFF5F7FA),
    surface = Color.White,
    surfaceVariant = Color(0xFFE8EEF5)
)

@Composable
fun AvisosCampusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresAvisos,
        typography = Typography,
        content = content
    )
}