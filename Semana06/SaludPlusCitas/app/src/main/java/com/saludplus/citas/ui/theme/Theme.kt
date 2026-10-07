package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulPrimario,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,
    secondary = AzulOscuro,
    background = FondoApp,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = AzulClaro,
    onSurfaceVariant = TextoSecundario,
    outline = BordeSuave,
    error = RojoError
)

@Composable
fun SaludPlusCitasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        content = content
    )
}