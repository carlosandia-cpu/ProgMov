package com.tecsup.lavacampus.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.estudiante.LoginScreen
import com.tecsup.lavacampus.ui.estudiante.SplashScreen
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@Composable
fun NavLava(sesionVm: SesionViewModel) {
    val nav = rememberNavController()

    fun irALogin() {
        sesionVm.cerrarSesion()
        nav.navigate(Rutas.LOGIN) {
            popUpTo(nav.graph.id) { inclusive = true }
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

        // Provisionales: se reemplazan en los pasos siguientes
        composable(Rutas.INICIO) { Provisional("Inicio del estudiante", ::irALogin) }
        composable(Rutas.PANEL) { Provisional("Panel del administrador", ::irALogin) }
    }
}

@Composable
private fun Provisional(titulo: String, onSalir: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(titulo, style = MaterialTheme.typography.titleLarge)
        Button(onClick = onSalir, modifier = Modifier.padding(top = 16.dp)) {
            Text("Cerrar sesión")
        }
    }
}