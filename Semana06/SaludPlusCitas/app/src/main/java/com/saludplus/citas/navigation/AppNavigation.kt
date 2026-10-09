package com.saludplus.citas.navigation

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.MenuLateral
import com.saludplus.citas.ui.components.PantallaEnConstruccion
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.citas.MisCitasScreen
import com.saludplus.citas.ui.screens.doctores.DoctoresScreen
import com.saludplus.citas.ui.screens.home.HomeScreen
import com.saludplus.citas.ui.screens.sedes.SedeCitasScreen
import com.saludplus.citas.ui.screens.sedes.SedesScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val entradaActual by navController.currentBackStackEntryAsState()
    val rutaActual = entradaActual?.destination?.route
    val menuActivo = rutaActual == Rutas.HOME || rutaActual == Rutas.SEDES ||
            rutaActual == Rutas.DOCTORES || rutaActual == Rutas.MIS_CITAS ||
            rutaActual?.startsWith("sede_citas") == true

    // Tras login o registro se entra a Inicio y se borra el flujo de autenticación
    val irAInicio = {
        navController.navigate(Rutas.HOME) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
        }
    }
    val volver: () -> Unit = { navController.popBackStack() }
    val abrirMenu: () -> Unit = { scope.launch { drawerState.open() } }

    // Destinos del menú lateral: siempre dejan la pila limpia (Inicio + el destino)
    val irAlMenu: (String) -> Unit = { ruta ->
        scope.launch { drawerState.close() }
        navController.navigate(ruta) {
            popUpTo(Rutas.HOME)
            launchSingleTop = true
        }
    }

    val cerrarSesion: () -> Unit = {
        scope.launch { drawerState.close() }
        Repositorio.cerrarSesion()
        navController.navigate(Rutas.SPLASH) {
            popUpTo(navController.graph.id) { inclusive = true }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = menuActivo,
        drawerContent = {
            MenuLateral(
                rutaActual = rutaActual,
                onDestino = irAlMenu,
                onCerrarSesion = cerrarSesion
            )
        }
    ) {
        NavHost(navController = navController, startDestination = Rutas.SPLASH) {

            composable(Rutas.SPLASH) {
                SplashScreen(
                    onCrearCuenta = { navController.navigate(Rutas.REGISTRO) },
                    onIniciarSesion = { navController.navigate(Rutas.LOGIN) }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onAtras = volver,
                    onVerTerminos = { navController.navigate(Rutas.TERMINOS) },
                    onRegistrado = { irAInicio() }
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    onAtras = volver,
                    onIrRegistro = { navController.navigate(Rutas.REGISTRO) },
                    onLoginExitoso = { irAInicio() }
                )
            }

            composable(Rutas.TERMINOS) {
                PantallaEnConstruccion(
                    titulo = "Términos y condiciones",
                    acciones = listOf("Volver" to volver)
                )
            }

            // ---- Destinos del menú lateral ----

            composable(Rutas.HOME) {
                HomeScreen(
                    onMenu = abrirMenu,
                    onSedes = { irAlMenu(Rutas.SEDES) },
                    onDoctores = { irAlMenu(Rutas.DOCTORES) },
                    onAgenda = { irAlMenu(Rutas.MIS_CITAS) },
                    onSede = { id -> navController.navigate(Rutas.sedeCitas(id)) }
                )
            }

            composable(Rutas.SEDES) {
                SedesScreen(
                    onMenu = abrirMenu,
                    onSede = { id -> navController.navigate(Rutas.sedeCitas(id)) }
                )
            }

            composable(Rutas.DOCTORES) {
                DoctoresScreen(
                    onMenu = abrirMenu,
                    onMedico = { id -> navController.navigate(Rutas.fechaHora(id)) }
                )
            }

            composable(Rutas.MIS_CITAS) {
                MisCitasScreen(
                    onMenu = abrirMenu,
                    onAgendar = { irAlMenu(Rutas.SEDES) }
                )
            }

            // ---- Dentro de una sede ----

            composable(
                route = Rutas.SEDE_CITAS,
                arguments = listOf(navArgument(Rutas.ARG_SEDE_ID) { type = NavType.IntType })
            ) { entrada ->
                val sedeId = entrada.arguments?.getInt(Rutas.ARG_SEDE_ID) ?: 0
                SedeCitasScreen(
                    sedeId = sedeId,
                    onAtras = volver,
                    onMedico = { id -> navController.navigate(Rutas.fechaHora(id)) }
                )
            }

            composable(
                route = Rutas.FECHA_HORA,
                arguments = listOf(navArgument(Rutas.ARG_MEDICO_ID) { type = NavType.IntType })
            ) { entrada ->
                val medicoId = entrada.arguments?.getInt(Rutas.ARG_MEDICO_ID) ?: 0
                FechaHoraScreen(
                    medicoId = medicoId,
                    onAtras = volver,
                    onContinuar = { fecha, hora ->
                        navController.navigate(Rutas.confirmar(medicoId, fecha, hora))
                    }
                )
            }

            composable(
                route = Rutas.CONFIRMAR,
                arguments = listOf(
                    navArgument(Rutas.ARG_MEDICO_ID) { type = NavType.IntType },
                    navArgument(Rutas.ARG_FECHA) { type = NavType.StringType },
                    navArgument(Rutas.ARG_HORA) { type = NavType.StringType }
                )
            ) { entrada ->
                val medicoId = entrada.arguments?.getInt(Rutas.ARG_MEDICO_ID) ?: 0
                val fecha = entrada.arguments?.getString(Rutas.ARG_FECHA) ?: ""
                val hora = entrada.arguments?.getString(Rutas.ARG_HORA) ?: ""
                ConfirmarCitaScreen(
                    medicoId = medicoId,
                    fecha = fecha,
                    hora = hora,
                    onAtras = volver,
                    onConfirmado = { citaId ->
                        // popUpTo: borra el flujo de agendamiento; Atrás vuelve a Inicio
                        navController.navigate(Rutas.citaExitosa(citaId)) {
                            popUpTo(Rutas.HOME)
                        }
                    }
                )
            }

            composable(
                route = Rutas.CITA_EXITOSA,
                arguments = listOf(navArgument(Rutas.ARG_CITA_ID) { type = NavType.IntType })
            ) { entrada ->
                val citaId = entrada.arguments?.getInt(Rutas.ARG_CITA_ID) ?: 0
                CitaExitosaScreen(
                    citaId = citaId,
                    onVerMisCitas = { irAlMenu(Rutas.MIS_CITAS) },
                    onIrInicio = { irAlMenu(Rutas.HOME) }
                )
            }
        }
    }
}