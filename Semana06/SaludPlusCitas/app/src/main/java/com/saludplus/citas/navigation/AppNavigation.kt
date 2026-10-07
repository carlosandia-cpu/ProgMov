package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraNavegacionInferior
import com.saludplus.citas.ui.components.PantallaEnConstruccion
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.MedicosScreen
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen
import com.saludplus.citas.ui.screens.home.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Al entrar a la app se borra el flujo de autenticación del historial
    val irAHome = {
        navController.navigate(Rutas.HOME) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
        }
    }

    val volver: () -> Unit = { navController.popBackStack() }

    // Cambio entre los 4 destinos del menú inferior sin apilar pantallas repetidas
    val irATab: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Rutas.HOME) { saveState = true }
            launchSingleTop = true
            restoreState = true
        }
    }

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
                onRegistrado = { irAHome() }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onAtras = volver,
                onIrRegistro = { navController.navigate(Rutas.REGISTRO) },
                onLoginExitoso = { irAHome() }
            )
        }

        composable(Rutas.TERMINOS) {
            PantallaEnConstruccion(
                titulo = "Términos y condiciones",
                acciones = listOf("Volver" to volver)
            )
        }

        // ---- Los 4 destinos del menú inferior ----

        composable(Rutas.HOME) {
            HomeScreen(
                onAgendar = { navController.navigate(Rutas.ESPECIALIDADES) },
                onMisCitas = { irATab(Rutas.MIS_CITAS) },
                onVerEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) },
                onEspecialidad = { id -> navController.navigate(Rutas.medicos(id)) },
                onNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) },
                onNavegar = irATab
            )
        }

        // Provisional: se reemplaza por MisCitasScreen en el commit 9
        composable(Rutas.MIS_CITAS) {
            PantallaEnConstruccion(
                titulo = "Mis citas",
                barraInferior = { BarraNavegacionInferior(Rutas.MIS_CITAS, irATab) }
            )
        }

        // Provisional: reto extra
        composable(Rutas.RESULTADOS) {
            PantallaEnConstruccion(
                titulo = "Resultados",
                barraInferior = { BarraNavegacionInferior(Rutas.RESULTADOS, irATab) }
            )
        }

        // Provisional: se reemplaza por PerfilScreen en el commit 9
        composable(Rutas.PERFIL) {
            PantallaEnConstruccion(
                titulo = "Perfil de ${Repositorio.usuarioActual?.nombre ?: "invitado"}",
                acciones = listOf(
                    "Cerrar sesión" to {
                        Repositorio.cerrarSesion()
                        navController.navigate(Rutas.SPLASH) {
                            popUpTo(Rutas.HOME) { inclusive = true }
                        }
                    }
                ),
                barraInferior = { BarraNavegacionInferior(Rutas.PERFIL, irATab) }
            )
        }

        // ---- Flujo de agendamiento ----

        composable(Rutas.ESPECIALIDADES) {
            EspecialidadesScreen(
                onAtras = volver,
                onEspecialidad = { id -> navController.navigate(Rutas.medicos(id)) }
            )
        }

        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument(Rutas.ARG_ESPECIALIDAD_ID) { type = NavType.IntType })
        ) { entrada ->
            val especialidadId = entrada.arguments?.getInt(Rutas.ARG_ESPECIALIDAD_ID) ?: 0
            MedicosScreen(
                especialidadId = especialidadId,
                onAtras = volver,
                onMedico = { medicoId -> navController.navigate(Rutas.fechaHora(medicoId)) }
            )
        }

        // Provisional: se reemplaza por FechaHoraScreen en el commit 7
        composable(
            route = Rutas.FECHA_HORA,
            arguments = listOf(navArgument(Rutas.ARG_MEDICO_ID) { type = NavType.IntType })
        ) { entrada ->
            val medicoId = entrada.arguments?.getInt(Rutas.ARG_MEDICO_ID) ?: 0
            val nombre = Repositorio.obtenerMedico(medicoId)?.nombre ?: "desconocido"
            PantallaEnConstruccion(
                titulo = "Fecha y hora con $nombre",
                acciones = listOf("Volver" to volver)
            )
        }

        composable(Rutas.NOTIFICACIONES) {
            PantallaEnConstruccion(
                titulo = "Notificaciones",
                acciones = listOf("Volver" to volver)
            )
        }
    }
}