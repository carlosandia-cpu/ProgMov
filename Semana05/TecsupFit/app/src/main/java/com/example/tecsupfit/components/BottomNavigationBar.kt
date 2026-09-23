package com.example.tecsupfit.components

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

data class BottomDestination(
    val route: String,
    val label: String,
    val symbol: String
)

val bottomDestinations = listOf(
    BottomDestination("home", "Inicio", "⌂"),
    BottomDestination("reservas", "Reservas", "✓"),
    BottomDestination("rutinas", "Rutinas", "◆"),
    BottomDestination("perfil", "Perfil", "●")
)

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        bottomDestinations.forEach { destination ->
            NavigationBarItem(
                selected = currentRoute == destination.route,
                onClick = {
                    onNavigate(destination.route)
                },
                icon = {
                    Text(destination.symbol)
                },
                label = {
                    Text(destination.label)
                }
            )
        }
    }
}