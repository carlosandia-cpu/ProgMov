package com.example.tecsupfit.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tecsupfit.components.BottomNavigationBar
import com.example.tecsupfit.components.bottomDestinations
import com.example.tecsupfit.data.clasesDisponibles
import com.example.tecsupfit.data.obtenerHorarios
import com.example.tecsupfit.model.Reserva
import com.example.tecsupfit.screens.ConfirmationScreen
import com.example.tecsupfit.screens.DetailScreen
import com.example.tecsupfit.screens.HomeScreen
import com.example.tecsupfit.screens.ProfileScreen
import com.example.tecsupfit.screens.ReservationsScreen
import com.example.tecsupfit.screens.RoutinesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val reservas = remember {
        mutableStateListOf<Reserva>()
    }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val bottomBarRoutes = bottomDestinations.map {
        it.route
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomBarRoutes) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo("home") {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(route = "home") {
                HomeScreen(
                    onClaseClick = { clase ->
                        navController.navigate("detalle/${clase.id}")
                    }
                )
            }

            composable(route = "reservas") {
                ReservationsScreen(
                    reservas = reservas
                )
            }

            composable(route = "rutinas") {
                RoutinesScreen()
            }

            composable(route = "perfil") {
                ProfileScreen()
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

                            val horarios = obtenerHorarios(clase)

                            val horarioSeleccionado = horarios.getOrElse(
                                horarioIndice
                            ) {
                                clase.horario
                            }

                            reservas.removeAll {
                                it.clase.id == clase.id
                            }

                            reservas.add(
                                Reserva(
                                    clase = clase,
                                    horario = horarioSeleccionado
                                )
                            )

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

                    val horarioSeleccionado = horarios.getOrElse(
                        horarioIndice
                    ) {
                        clase.horario
                    }

                    ConfirmationScreen(
                        clase = clase,
                        horarioSeleccionado = horarioSeleccionado,
                        onVerReservas = {
                            navController.navigate("reservas") {
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
}

