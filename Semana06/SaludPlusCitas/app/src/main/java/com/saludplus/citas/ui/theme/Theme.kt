package com.saludplus.citas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = TealPrimario,
    onPrimary = Color.White,
    primaryContainer = MentaClaro,
    onPrimaryContainer = TealOscuro,
    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = MentaClaro,
    onSecondaryContainer = TealOscuro,
    background = FondoApp,
    onBackground = TextoPrincipal,
    surface = Color.White,
    onSurface = TextoPrincipal,
    surfaceVariant = MentaClaro,
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