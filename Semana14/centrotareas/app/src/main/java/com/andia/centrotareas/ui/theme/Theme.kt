
package com.andia.centrotareas.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ColoresClaros = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Blanco,
    primaryContainer = AzulSuave,
    onPrimaryContainer = AzulNoche,
    secondary = Turquesa,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = Blanco,
    onSurface = TextoOscuro,
    surfaceVariant = Color(0xFFE8EEF5),
    onSurfaceVariant = TextoSuave,
    outline = Color(0xFFD1DCE6),
    error = TextoError,
    errorContainer = FondoError,
    onErrorContainer = TextoError
)

private val ColoresOscuros = darkColorScheme(
    primary = Turquesa,
    onPrimary = AzulNoche,
    secondary = Color(0xFF9FD9EA),
    background = Color(0xFF0C1728),
    onBackground = Color(0xFFECF3FA),
    surface = Color(0xFF172A43),
    onSurface = Color(0xFFECF3FA),
    surfaceVariant = Color(0xFF273E59),
    onSurfaceVariant = Color(0xFFC5D3E2),
    outline = Color(0xFF536985),
    error = Color(0xFFFFA5A2),
    errorContainer = Color(0xFF572A31),
    onErrorContainer = Color(0xFFFFDEDB)
)

@Composable
fun CentroTareasTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) {
            ColoresOscuros
        } else {
            ColoresClaros
        },
        typography = Typography,
        content = content
    )
}
