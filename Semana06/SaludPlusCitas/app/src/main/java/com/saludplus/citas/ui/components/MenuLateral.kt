package com.saludplus.citas.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.navigation.Rutas

@Composable
fun MenuLateral(
    rutaActual: String?,
    onDestino: (String) -> Unit,
    onCerrarSesion: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val inicial = usuario?.nombre?.firstOrNull()?.uppercase() ?: "?"

    ModalDrawerSheet(windowInsets = WindowInsets(0, 0, 0, 0)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding()
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.onPrimary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = usuario?.nombre ?: "Invitado",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = usuario?.correo ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
            )
        }

        Spacer(Modifier.height(12.dp))

        ItemMenu("Inicio", Icons.Default.Home, rutaActual == Rutas.HOME) {
            onDestino(Rutas.HOME)
        }
        ItemMenu(
            "Sede", Icons.Default.LocationOn,
            rutaActual == Rutas.SEDES || rutaActual?.startsWith("sede_citas") == true
        ) { onDestino(Rutas.SEDES) }
        ItemMenu("Doctor", Icons.Default.MedicalServices, rutaActual == Rutas.DOCTORES) {
            onDestino(Rutas.DOCTORES)
        }
        ItemMenu("Agenda", Icons.Default.CalendarMonth, rutaActual == Rutas.MIS_CITAS) {
            onDestino(Rutas.MIS_CITAS)
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp))

        ItemMenu("Cerrar sesión", Icons.AutoMirrored.Filled.ExitToApp, false, onCerrarSesion)
    }
}

@Composable
private fun ItemMenu(
    texto: String,
    icono: ImageVector,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = { Text(texto) },
        icon = { Icon(icono, contentDescription = null) },
        selected = seleccionado,
        onClick = onClick,
        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
    )
}