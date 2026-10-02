package com.andia.lab04carritotecsup

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

object Rutas {
    const val INICIO = "inicio"
    const val PEDIDOS = "pedidos"
    const val FAVORITOS = "favoritos"
    const val PERFIL = "perfil"
}

@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Destino que se está mostrando actualmente
    var destinoActual by remember { mutableStateOf(Rutas.INICIO) }

    // Estado compartido: vive aquí para no perderse al cambiar de pantalla
    val productos = remember { mutableStateListOf<Producto>() }
    val favoritos = remember { mutableStateListOf<Producto>() }

    val titulo = when (destinoActual) {
        Rutas.PEDIDOS -> "Mis pedidos"
        Rutas.FAVORITOS -> "Favoritos"
        Rutas.PERFIL -> "Perfil"
        else -> "TECSUP Store"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                destinoActual = destinoActual,
                onDestinoClick = { ruta ->
                    destinoActual = ruta
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                BarraSuperior(
                    titulo = titulo,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    }
                )
            }
        ) { innerPadding ->
            val modifier = Modifier.padding(innerPadding)

            when (destinoActual) {
                Rutas.INICIO -> PantallaCarrito(
                    modifier = modifier,
                    productos = productos,
                    favoritos = favoritos
                )
                Rutas.PEDIDOS -> PantallaPedidos(modifier = modifier)
                Rutas.FAVORITOS -> PantallaFavoritos(
                    modifier = modifier,
                    favoritos = favoritos
                )
                Rutas.PERFIL -> PantallaPerfil(modifier = modifier)
                else -> {}
            }
        }
    }
}

@Composable
private fun BarraSuperior(
    titulo: String,
    onMenuClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(horizontal = 4.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Abrir menú",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }

            Text(
                text = titulo,
                modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}