package com.saludplus.citas.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun LoginScreen(
    onAtras: () -> Unit,
    onIrRegistro: () -> Unit,
    onLoginExitoso: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { BarraSuperior("Iniciar sesión", onAtras) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido de nuevo",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(8.dp))

            CampoTexto(
                valor = correo,
                onCambio = { correo = it; error = null },
                etiqueta = "Correo electrónico",
                teclado = KeyboardType.Email
            )
            CampoTexto(
                valor = contrasena,
                onCambio = { contrasena = it; error = null },
                etiqueta = "Contraseña",
                esContrasena = true
            )

            if (error != null) {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(8.dp))

            BotonPrimario(
                texto = "Ingresar",
                onClick = {
                    if (correo.isBlank() || contrasena.isBlank()) {
                        error = "Completa todos los campos"
                    } else if (Repositorio.iniciarSesion(correo, contrasena)) {
                        onLoginExitoso()
                    } else {
                        error = "Correo o contraseña incorrectos"
                    }
                }
            )

            TextButton(onClick = onIrRegistro) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}