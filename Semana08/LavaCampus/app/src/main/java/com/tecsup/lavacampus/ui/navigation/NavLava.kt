package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.administrador.AjustesScreen
import com.tecsup.lavacampus.ui.administrador.BandejaScreen
import com.tecsup.lavacampus.ui.administrador.ClientesScreen
import com.tecsup.lavacampus.ui.administrador.FormularioServicioScreen
import com.tecsup.lavacampus.ui.administrador.GestionPedidoScreen
import com.tecsup.lavacampus.ui.administrador.PanelScreen
import com.tecsup.lavacampus.ui.administrador.ServiciosScreen
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.estudiante.DetallePedidoScreen
import com.tecsup.lavacampus.ui.estudiante.InicioScreen
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.MisPedidosScreen
import com.tecsup.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.tecsup.lavacampus.ui.estudiante.PerfilScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.ui.estudiante.TarifasScreen
import com.tecsup.lavacampus.viewmodel.BandejaViewModel
import com.tecsup.lavacampus.viewmodel.ClientesViewModel
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel
import com.tecsup.lavacampus.viewmodel.InicioViewModel
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel
import com.tecsup.lavacampus.viewmodel.PanelViewModel
import com.tecsup.lavacampus.viewmodel.PerfilViewModel
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
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

    /** Guarda el otro perfil, navega a su grafo y limpia la pila de navegación. */
    fun cambiarPerfil(destino: Rol) {
        sesionVm.cambiarPerfil(destino) { rol ->
            nav.navigate(if (rol == Rol.ADMIN) Rutas.PANEL else Rutas.INICIO) {
                popUpTo(nav.graph.id) { inclusive = true }
            }
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
                        onNuevoPedido = { nav.navigate(Rutas.nuevoPedido()) },
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
                        FloatingActionButton(onClick = { nav.navigate(Rutas.nuevoPedido()) }) {
                            Icon(Icons.Default.Add, contentDescription = "Nuevo pedido")
                        }
                    }
                ) { padding ->
                    MisPedidosScreen(
                        vm = vm,
                        modifier = Modifier.padding(padding),
                        onDetalle = { id -> nav.navigate(Rutas.detalle(id)) },
                        onPedirDeNuevo = { id -> nav.navigate(Rutas.nuevoPedido(id)) }
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
            usuario?.let { u ->
                val vm: PerfilViewModel = viewModel(factory = fabrica(usuarioId = u.id))
                ContenedorEstudiante(rutaActual = Rutas.PERFIL, onIr = { irATab(it) }, titulo = "Perfil") { padding ->
                    PerfilScreen(
                        usuario = u,
                        vm = vm,
                        sesionVm = sesionVm,
                        modifier = Modifier.padding(padding),
                        onCambiarPerfil = { cambiarPerfil(Rol.ADMIN) },
                        onCerrarSesion = { irALogin() }
                    )
                }
            }
        }
        composable(
            route = Rutas.NUEVO_PEDIDO,
            arguments = listOf(navArgument("pedidoId") {
                type = NavType.IntType
                defaultValue = 0
            })
        ) { entrada ->
            val pedidoId = entrada.arguments?.getInt("pedidoId") ?: 0
            usuario?.let { u ->
                val vm: NuevoPedidoViewModel = viewModel(factory = fabrica(usuarioId = u.id, pedidoId = pedidoId))
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
        composable(Rutas.SERVICIOS) {
            val vm: ServiciosViewModel = viewModel(factory = fabrica())
            ContenedorAdmin(
                rutaActual = Rutas.SERVICIOS,
                onIr = { irATab(it, Rutas.PANEL) },
                titulo = "Servicios",
                fab = {
                    FloatingActionButton(onClick = { nav.navigate(Rutas.formulario(0)) }) {
                        Icon(Icons.Default.Add, contentDescription = "Nuevo servicio")
                    }
                }
            ) { padding ->
                ServiciosScreen(
                    vm = vm,
                    modifier = Modifier.padding(padding),
                    onEditar = { id -> nav.navigate(Rutas.formulario(id)) }
                )
            }
        }
        composable(
            route = Rutas.FORMULARIO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt("id") ?: 0
            val vm: ServiciosViewModel = viewModel(factory = fabrica())
            FormularioServicioScreen(vm = vm, id = id, onAtras = { nav.popBackStack() })
        }
        composable(Rutas.CLIENTES) {
            val vm: ClientesViewModel = viewModel(factory = fabrica())
            ContenedorAdmin(
                rutaActual = Rutas.CLIENTES,
                onIr = { irATab(it, Rutas.PANEL) },
                titulo = "Clientes"
            ) { padding ->
                ClientesScreen(vm = vm, modifier = Modifier.padding(padding))
            }
        }
        composable(Rutas.AJUSTES) {
            usuario?.let { u ->
                ContenedorAdmin(
                    rutaActual = Rutas.AJUSTES,
                    onIr = { irATab(it, Rutas.PANEL) },
                    titulo = "Ajustes"
                ) { padding ->
                    AjustesScreen(
                        usuario = u,
                        sesionVm = sesionVm,
                        modifier = Modifier.padding(padding),
                        onCambiarPerfil = { cambiarPerfil(Rol.CLIENTE) },
                        onCerrarSesion = { irALogin() }
                    )
                }
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
    fab: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = { if (titulo != null) AppTopBar(titulo = titulo) },
        floatingActionButton = fab,
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