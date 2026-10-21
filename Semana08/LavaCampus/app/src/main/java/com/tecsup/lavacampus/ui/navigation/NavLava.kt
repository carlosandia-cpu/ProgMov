package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.BandejaScreen
import com.tecsup.lavacampus.ui.administrador.GestionPedidoScreen
import com.tecsup.lavacampus.ui.administrador.PanelScreen
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.estudiante.DetallePedidoScreen
import com.tecsup.lavacampus.ui.estudiante.InicioScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.MisPedidosScreen
import com.tecsup.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.TarifasScreen
import com.tecsup.lavacampus.viewmodel.BandejaViewModel
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel
import com.tecsup.lavacampus.viewmodel.InicioViewModel
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
import com.tecsup.lavacampus.viewmodel.PanelViewModel
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import com.tecsup.lavacampus.viewmodel.TarifasViewModel

private data class ItemNav(val ruta: String, val titulo: String, val icono: ImageVector)

private val itemsEstudiante = listOf(
    ItemNav(Rutas.INICIO, "Inicio", Icons.Default.Home),
    ItemNav(Rutas.MIS_PEDIDOS, "Pedidos", Icons.Default.ShoppingBag),
    ItemNav(Rutas.TARIFAS, "Tarifas", Icons.Default.LocalOffer),
    ItemNav(Rutas.PERFIL, "Perfil", Icons.Default.Person)
)

private val itemsAdmin = listOf(
    ItemNav(Rutas.PANEL, "Panel", Icons.Default.Dashboard),
    ItemNav(Rutas.BANDEJA, "Pedidos", Icons.Default.Inbox),
    ItemNav(Rutas.SERVICIOS, "Servicios", Icons.Default.LocalLaundryService),
    ItemNav(Rutas.CLIENTES, "Clientes", Icons.Default.People),
    ItemNav(Rutas.AJUSTES, "Ajustes", Icons.Default.Settings)
)

@Composable
fun NavLava(
    sesionVm: SesionViewModel,
    repo: LavaRepository,
    prefs: PreferenciasStore
) {
    val nav = rememberNavController()
    val usuario by sesionVm.usuarioActual.collectAsState()

    fun fabrica(usuarioId: Int = 0, pedidoId: Int = 0) =
        LavaViewModelFactory(repo, prefs, usuarioId, pedidoId)

    fun irALogin() {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
        }
    }

    fun irATab(ruta: String, inicio: String = Rutas.INICIO) {
        nav.navigate(ruta) {
            popUpTo(inicio) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(navController = nav, startDestination = Rutas.SPLASH) {
        composable(Rutas.SPLASH) {
            SplashScreen(sesionVm) { destino ->
                nav.navigate(destino) { popUpTo(Rutas.SPLASH) { inclusive = true } }
            }
        }
        composable(Rutas.LOGIN) {
            LoginScreen(sesionVm) { rol ->
                val destino = if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO
                nav.navigate(destino) { popUpTo(Rutas.LOGIN) { inclusive = true } }
            }
        }

        // ---------- Estudiante ----------
        composable(Rutas.INICIO) {
            usuario?.let { u ->
                val vm: InicioViewModel = viewModel(factory = fabrica(usuarioId = u.id))
                ContenedorEstudiante(rutaActual = Rutas.INICIO, onIr = { irATab(it) }) { padding ->
                    InicioScreen(
                        usuario = u,
                        vm = vm,
                        modifier = Modifier.padding(padding),
                        onNuevoPedido = { nav.navigate(Rutas.NUEVO_PEDIDO) },
                        onMisPedidos = { irATab(Rutas.MIS_PEDIDOS) },
                        onTarifas = { irATab(Rutas.TARIFAS) },
                        onDetalle = { id -> nav.navigate(Rutas.detalle(id)) }
                    )
                }
            }
        }
        composable(Rutas.MIS_PEDIDOS) {
            usuario?.let { u ->
                val vm: MisPedidosViewModel = viewModel(factory = fabrica(usuarioId = u.id))
                ContenedorEstudiante(
                    rutaActual = Rutas.MIS_PEDIDOS,
                    onIr = { irATab(it) },
                    titulo = "Mis pedidos",
                    fab = {
                        FloatingActionButton(onClick = { nav.navigate(Rutas.NUEVO_PEDIDO) }) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo pedido")
                        }
                    }
                ) { padding ->
                    MisPedidosScreen(
                        vm = vm,
                        modifier = Modifier.padding(padding),
                        onDetalle = { id -> nav.navigate(Rutas.detalle(id)) }
                    )
                }
            }
        }
        composable(Rutas.TARIFAS) {
            val vm: TarifasViewModel = viewModel(factory = fabrica())
            ContenedorEstudiante(rutaActual = Rutas.TARIFAS, onIr = { irATab(it) }, titulo = "Tarifas") { padding ->
                TarifasScreen(vm = vm, modifier = Modifier.padding(padding))
            }
        }
        composable(Rutas.PERFIL) {
            ContenedorEstudiante(rutaActual = Rutas.PERFIL, onIr = { irATab(it) }, titulo = "Perfil") { padding ->
                Provisional("Perfil del estudiante", Modifier.padding(padding), { irALogin() })
            }
        }
        composable(Rutas.NUEVO_PEDIDO) {
            usuario?.let { u ->
                val vm: NuevoPedidoViewModel = viewModel(factory = fabrica(usuarioId = u.id))
                NuevoPedidoScreen(
                    vm = vm,
                    onAtras = { nav.popBackStack() },
                    onCreado = { nav.navigate(Rutas.MIS_PEDIDOS) { popUpTo(Rutas.INICIO) } }
                )
            }
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: DetallePedidoViewModel = viewModel(factory = fabrica(pedidoId = id))
            DetallePedidoScreen(vm = vm, onAtras = { nav.popBackStack() })
        }

        // ---------- Administrador ----------
        composable(Rutas.PANEL) {
            val vm: PanelViewModel = viewModel(factory = fabrica())
            ContenedorAdmin(rutaActual = Rutas.PANEL, onIr = { irATab(it, Rutas.PANEL) }) { padding ->
                PanelScreen(
                    vm = vm,
                    modifier = Modifier.padding(padding),
                    onVerListos = { irATab(Rutas.BANDEJA, Rutas.PANEL) }
                )
            }
        }
        composable(Rutas.BANDEJA) {
            val vm: BandejaViewModel = viewModel(factory = fabrica())
            ContenedorAdmin(
                rutaActual = Rutas.BANDEJA,
                onIr = { irATab(it, Rutas.PANEL) },
                titulo = "Pedidos"
            ) { padding ->
                BandejaScreen(
                    vm = vm,
                    modifier = Modifier.padding(padding),
                    onGestion = { id -> nav.navigate(Rutas.gestion(id)) }
                )
            }
        }
        composable(
            route = Rutas.GESTION,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: GestionPedidoViewModel = viewModel(factory = fabrica(pedidoId = id))
            GestionPedidoScreen(vm = vm, onAtras = { nav.popBackStack() })
        }

        // Provisionales hasta el commit 14
        composable(Rutas.SERVICIOS) {
            ContenedorAdmin(rutaActual = Rutas.SERVICIOS, onIr = { irATab(it, Rutas.PANEL) }, titulo = "Servicios") { padding ->
                Provisional("Servicios", Modifier.padding(padding), null)
            }
        }
        composable(Rutas.CLIENTES) {
            ContenedorAdmin(rutaActual = Rutas.CLIENTES, onIr = { irATab(it, Rutas.PANEL) }, titulo = "Clientes") { padding ->
                Provisional("Clientes", Modifier.padding(padding), null)
            }
        }
        composable(Rutas.AJUSTES) {
            ContenedorAdmin(rutaActual = Rutas.AJUSTES, onIr = { irATab(it, Rutas.PANEL) }, titulo = "Ajustes") { padding ->
                Provisional("Ajustes", Modifier.padding(padding), { irALogin() })
            }
        }
    }
}

@Composable
private fun ContenedorEstudiante(
    rutaActual: String,
    onIr: (String) -> Unit,
    titulo: String? = null,
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { if (titulo != null) AppTopBar(titulo = titulo) },
        floatingActionButton = fab,
        bottomBar = { BarraInferior(itemsEstudiante, rutaActual, onIr) },
        content = content
    )
}

@Composable
private fun ContenedorAdmin(
    rutaActual: String,
    onIr: (String) -> Unit,
    titulo: String? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { if (titulo != null) AppTopBar(titulo = titulo) },
        bottomBar = { BarraInferior(itemsAdmin, rutaActual, onIr) },
        content = content
    )
}

@Composable
private fun BarraInferior(items: List<ItemNav>, rutaActual: String, onIr: (String) -> Unit) {
    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = rutaActual == item.ruta,
                onClick = { if (rutaActual != item.ruta) onIr(item.ruta) },
                icon = { Icon(item.icono, contentDescription = item.titulo) },
                label = { Text(item.titulo) }
            )
        }
    }
}

@Composable
private fun Provisional(titulo: String, modifier: Modifier, onSalir: (() -> Unit)?) {
    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(titulo, style = MaterialTheme.typography.titleLarge)
        if (onSalir != null) {
            Button(onClick = onSalir, modifier = Modifier.padding(top = 16.dp)) {
                Text("Cerrar sesión")
            }
        }
    }
}