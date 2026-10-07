package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.PantallaEnConstruccion
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

    val volver = { navController.popBackStack() }

    NavHost(navController = navController, startDestination = Rutas.SPLASH) {

        composable(Rutas.SPLASH) {
            SplashScreen(
                onCrearCuenta = { navController.navigate(Rutas.REGISTRO) },
                onIniciarSesion = { navController.navigate(Rutas.LOGIN) }
            )
        }

        composable(Rutas.REGISTRO) {
            RegistroScreen(
                onAtras = { navController.popBackStack() },
                onVerTerminos = { navController.navigate(Rutas.TERMINOS) },
                onRegistrado = { irAHome() }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onAtras = { navController.popBackStack() },
                onIrRegistro = { navController.navigate(Rutas.REGISTRO) },
                onLoginExitoso = { irAHome() }
            )
        }

        composable(Rutas.TERMINOS) {
            PantallaEnConstruccion(
                titulo = "Términos y condiciones",
                acciones = listOf("Volver" to { volver() })
            )
        }

        composable(Rutas.HOME) {
            HomeScreen(
                onAgendar = { navController.navigate(Rutas.ESPECIALIDADES) },
                onMisCitas = { navController.navigate(Rutas.MIS_CITAS) },
                onVerEspecialidades = { navController.navigate(Rutas.ESPECIALIDADES) },
                onEspecialidad = { id -> navController.navigate(Rutas.medicos(id)) },
                onNotificaciones = { navController.navigate(Rutas.NOTIFICACIONES) }
            )
        }

        // ---- Provisionales: se reemplazan en los siguientes commits ----

        composable(Rutas.ESPECIALIDADES) {
            PantallaEnConstruccion(
                titulo = "Especialidades",
                acciones = listOf("Volver" to { volver() })
            )
        }

        composable(
            route = Rutas.MEDICOS,
            arguments = listOf(navArgument(Rutas.ARG_ESPECIALIDAD_ID) { type = NavType.IntType })
        ) { entrada ->
            val id = entrada.arguments?.getInt(Rutas.ARG_ESPECIALIDAD_ID) ?: 0
            val nombre = Repositorio.obtenerEspecialidad(id)?.nombre ?: "desconocida"
            PantallaEnConstruccion(
                titulo = "Médicos de $nombre",
                acciones = listOf("Volver" to { volver() })
            )
        }

        composable(Rutas.MIS_CITAS) {
            PantallaEnConstruccion(
                titulo = "Mis citas",
                acciones = listOf("Volver" to { volver() })
            )
        }

        composable(Rutas.NOTIFICACIONES) {
            PantallaEnConstruccion(
                titulo = "Notificaciones",
                acciones = listOf("Volver" to { volver() })
            )
        }
    }
}