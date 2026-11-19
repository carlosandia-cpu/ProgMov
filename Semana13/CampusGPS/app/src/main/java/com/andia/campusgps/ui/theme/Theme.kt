
package com.andia.campusgps.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// Esquema institucional de colores
private val EsquemaCampus = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Blanco,

    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulNoche,

    secondary = VerdeCampus,
    onSecondary = Blanco,

    secondaryContainer = VerdeClaro,
    onSecondaryContainer = VerdeCampus,

    tertiary = AzulNoche,
    onTertiary = Blanco,

    background = FondoApp,
    onBackground = TextoPrincipal,

    surface = Blanco,
    onSurface = TextoPrincipal,

    surfaceVariant = AzulClaro,
    onSurfaceVariant = TextoSecundario,

    outline = BordeSuave,
    outlineVariant = BordeSuave,

    error = RojoError,
    onError = Blanco,

    errorContainer = FondoError,
    onErrorContainer = TextoError
)

// Bordes personalizados del diseño anterior
private val FormasCampus = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(30.dp)
)

@Composable
fun CampusGPSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaCampus,
        shapes = FormasCampus,
        content = content
    )
}
