
package com.andia.campusgps.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val CampusColorScheme = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Blanco,

    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulNoche,

    secondary = VerdeCampus,
    onSecondary = Blanco,

    background = FondoApp,
    onBackground = TextoPrincipal,

    surface = Blanco,
    onSurface = TextoPrincipal,

    surfaceVariant = AzulClaro,
    onSurfaceVariant = TextoSecundario,

    outline = BordeSuave,

    error = RojoError,
    errorContainer = FondoError
)

@Composable
fun CampusGPSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CampusColorScheme,
        content = content
    )
}
