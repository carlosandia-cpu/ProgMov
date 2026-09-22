package com.example.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tecsupfit.data.clasesDisponibles
import com.example.tecsupfit.data.obtenerHorarios
import com.example.tecsupfit.screens.ConfirmationScreen
import com.example.tecsupfit.screens.DetailScreen
import com.example.tecsupfit.screens.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(route = "home") {
            HomeScreen(
                onClaseClick = { clase ->
                    navController.navigate("detalle/${clase.id}")
                }
            )
        }

        composable(
            route = "detalle/{claseId}",
            arguments = listOf(
                navArgument("claseId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val claseId = backStackEntry.arguments
                ?.getInt("claseId")

            val claseSeleccionada = clasesDisponibles.find {
                it.id == claseId
            }

            claseSeleccionada?.let { clase ->
                DetailScreen(
                    clase = clase,
                    onVolver = {
                        navController.popBackStack()
                    },
                    onReservar = { horarioIndice ->
                        navController.navigate(
                            "confirmacion/${clase.id}/$horarioIndice"
                        )
                    }
                )
            }
        }

        composable(
            route = "confirmacion/{claseId}/{horarioIndice}",
            arguments = listOf(
                navArgument("claseId") {
                    type = NavType.IntType
                },
                navArgument("horarioIndice") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val claseId = backStackEntry.arguments
                ?.getInt("claseId")

            val horarioIndice = backStackEntry.arguments
                ?.getInt("horarioIndice") ?: 0

            val claseSeleccionada = clasesDisponibles.find {
                it.id == claseId
            }

            claseSeleccionada?.let { clase ->
                val horarios = obtenerHorarios(clase)
                val horarioSeleccionado =
                    horarios.getOrElse(horarioIndice) { clase.horario }

                ConfirmationScreen(
                    clase = clase,
                    horarioSeleccionado = horarioSeleccionado,
                    onVerReservas = {
                        navController.navigate("home") {
                            popUpTo("home") {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}