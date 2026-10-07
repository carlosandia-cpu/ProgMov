package com.saludplus.citas.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.PantallaEnConstruccion
import com.saludplus.citas.ui.screens.auth.LoginScreen
import com.saludplus.citas.ui.screens.auth.RegistroScreen
import com.saludplus.citas.ui.screens.auth.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Al entrar a la app se borra el flujo de autenticación del historial
    val irAHome = {
        navController.navigate(Rutas.HOME) {
            popUpTo(Rutas.SPLASH) { inclusive = true }
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
                acciones = listOf("Volver" to { navController.popBackStack() })
            )
        }

        // Temporal: se reemplaza por HomeScreen en el siguiente commit
        composable(Rutas.HOME) {
            PantallaEnConstruccion(
                titulo = "Inicio de ${Repositorio.usuarioActual?.nombre ?: "invitado"}",
                acciones = listOf(
                    "Cerrar sesión" to {
                        Repositorio.cerrarSesion()
                        navController.navigate(Rutas.SPLASH) {
                            popUpTo(Rutas.HOME) { inclusive = true }
                        }
                    }
                )
            )
        }
    }
}