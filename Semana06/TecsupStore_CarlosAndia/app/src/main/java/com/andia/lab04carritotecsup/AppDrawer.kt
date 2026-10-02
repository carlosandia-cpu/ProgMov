package com.andia.lab04carritotecsup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class DestinoDrawer(
    val ruta: String,
    val titulo: String,
    val icono: ImageVector
)

@Composable
fun AppDrawer(
    destinoActual: String,
    onDestinoClick: (String) -> Unit
) {
    val destinos = listOf(
        DestinoDrawer(Rutas.INICIO, "Inicio", Icons.Default.Home),
        DestinoDrawer(Rutas.PEDIDOS, "Mis pedidos", Icons.Default.ShoppingCart),
        DestinoDrawer(Rutas.FAVORITOS, "Favoritos", Icons.Default.Favorite),
        DestinoDrawer(Rutas.PERFIL, "Perfil", Icons.Default.Person)
    )

    ModalDrawerSheet {
        EncabezadoUsuario(
            nombre = "Carlos Andia",
            correo = "carlos.andia@tecsup.edu.pe"
        )

        Spacer(modifier = Modifier.height(8.dp))

        destinos.forEach { destino ->
            NavigationDrawerItem(
                label = { Text(destino.titulo) },
                icon = {
                    Icon(
                        imageVector = destino.icono,
                        contentDescription = null
                    )
                },
                selected = destinoActual == destino.ruta,
                onClick = { onDestinoClick(destino.ruta) },
                // Ítem activo con color de fondo distinto al resto
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun EncabezadoUsuario(
    nombre: String,
    correo: String
) {
    // Iniciales a partir del nombre (máximo 2 letras)
    val iniciales = nombre
        .split(" ")
        .mapNotNull { it.firstOrNull() }
        .take(2)
        .joinToString("")
        .uppercase()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iniciales,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = nombre,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Text(
            text = correo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}