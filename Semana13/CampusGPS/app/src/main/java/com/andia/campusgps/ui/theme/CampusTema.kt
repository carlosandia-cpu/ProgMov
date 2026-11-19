package com.andia.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

private val EsquemaCampus = lightColorScheme(
    primary = Color(0xFF1565C0),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD6E6FB),
    onPrimaryContainer = Color(0xFF0B3C7A),
    secondary = Color(0xFF2E9E4F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDDF3E4),
    onSecondaryContainer = Color(0xFF14532D),
    background = Color(0xFFF5F8FC),
    surface = Color.White,
    onSurface = Color(0xFF14202E),
    surfaceVariant = Color(0xFFE8EEF6),
    onSurfaceVariant = Color(0xFF55657A),
    outlineVariant = Color(0xFFD3DCE8),
    errorContainer = Color(0xFFFDE4E4),
    onErrorContainer = Color(0xFF8A1C1C)
)

private val FormasCampus = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(24.dp)
)

@Composable
fun CampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaCampus,
        shapes = FormasCampus,
        content = content
    )
}