package com.tecsup.lavacampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.lavacampus.model.Rol

private val EstudianteClaro = lightColorScheme(
    primary = Turquesa,
    onPrimary = Color.White,
    primaryContainer = TurquesaContenedor,
    onPrimaryContainer = Color(0xFF083344),
    background = TurquesaFondo,
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = TurquesaContenedor,
    onSurfaceVariant = Color(0xFF334155)
)

private val AdminClaro = lightColorScheme(
    primary = Violeta,
    onPrimary = Color.White,
    primaryContainer = VioletaContenedor,
    onPrimaryContainer = Color(0xFF2E1065),
    background = VioletaFondo,
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = VioletaContenedor,
    onSurfaceVariant = Color(0xFF334155)
)

private val EstudianteOscuro = darkColorScheme(
    primary = Color(0xFF22D3EE),
    onPrimary = Color(0xFF083344),
    primaryContainer = Color(0xFF155E75),
    onPrimaryContainer = TurquesaContenedor,
    background = Color(0xFF0B1A1F),
    onBackground = Color(0xFFE2F5F9),
    surface = Color(0xFF12262C),
    onSurface = Color(0xFFE2F5F9),
    surfaceVariant = Color(0xFF1C3A43),
    onSurfaceVariant = Color(0xFFB6D4DB)
)

private val AdminOscuro = darkColorScheme(
    primary = Color(0xFFA78BFA),
    onPrimary = Color(0xFF2E1065),
    primaryContainer = Color(0xFF4C1D95),
    onPrimaryContainer = VioletaContenedor,
    background = Color(0xFF14101F),
    onBackground = Color(0xFFEDE9FE),
    surface = Color(0xFF1D1730),
    onSurface = Color(0xFFEDE9FE),
    surfaceVariant = Color(0xFF2B2145),
    onSurfaceVariant = Color(0xFFCFC6E8)
)

@Composable
fun LavaTheme(
    rol: Rol = Rol.CLIENTE,
    modoOscuro: Boolean = false,
    content: @Composable () -> Unit
) {
    val esquema = when {
        rol == Rol.ADMIN && modoOscuro -> AdminOscuro
        rol == Rol.ADMIN -> AdminClaro
        modoOscuro -> EstudianteOscuro
        else -> EstudianteClaro
    }
    MaterialTheme(
        colorScheme = esquema,
        typography = Typography,
        content = content
    )
}