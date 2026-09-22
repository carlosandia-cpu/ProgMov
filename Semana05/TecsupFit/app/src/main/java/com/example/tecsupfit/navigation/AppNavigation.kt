package com.example.tecsupfit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tecsupfit.data.clasesDisponibles
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
                    navController.navigate(
                        route = "detalle/${clase.id}"
                    )
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

            val claseSeleccionada = clasesDisponibles.find { clase ->
                clase.id == claseId
            }

            if (claseSeleccionada != null) {
                DetailScreen(
                    clase = claseSeleccionada,
                    onBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}