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
import com.saludplus.citas.ui.screens.agendamiento.CitaExitosaScreen
import com.saludplus.citas.ui.screens.agendamiento.ConfirmarCitaScreen
import com.saludplus.citas.ui.screens.agendamiento.EspecialidadesScreen
import com.saludplus.citas.ui.screens.agendamiento.FechaHoraScreen
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
                    // popUpTo: borra todo el flujo de agendamiento del historial,
                    // de modo que Atrás desde la pantalla de éxito vuelve a Inicio
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
                onVerMisCitas = { irATab(Rutas.MIS_CITAS) },
                onIrInicio = { navController.popBackStack(Rutas.HOME, inclusive = false) }
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