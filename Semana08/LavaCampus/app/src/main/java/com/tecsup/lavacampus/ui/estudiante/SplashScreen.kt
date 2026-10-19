package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.navigation.Rutas
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(vm: SesionViewModel, onDestino: (String) -> Unit) {
    LaunchedEffect(Unit) {
        delay(1200)
        val rol = vm.restaurarSesion()
        onDestino(
            when (rol) {
                Rol.CLIENTE -> Rutas.INICIO
                Rol.ADMIN -> Rutas.PANEL
                null -> Rutas.LOGIN
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocalLaundryService,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "LavaCampus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                "Lavandería universitaria",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}