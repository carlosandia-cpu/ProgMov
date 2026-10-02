package com.andia.lab04carritotecsup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
        Text(
            text = "TECSUP Store",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider()

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
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
    }
}