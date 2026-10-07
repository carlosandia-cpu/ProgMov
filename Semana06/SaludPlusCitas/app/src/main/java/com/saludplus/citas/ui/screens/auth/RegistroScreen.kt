package com.saludplus.citas.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
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
import com.saludplus.citas.data.model.Usuario
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.components.CampoTexto

@Composable
fun RegistroScreen(
    onAtras: () -> Unit,
    onVerTerminos: () -> Unit,
    onRegistrado: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmar by rememberSaveable { mutableStateOf("") }
    var acepta by rememberSaveable { mutableStateOf(false) }
    var intentoEnviar by rememberSaveable { mutableStateOf(false) }
    var errorRegistro by rememberSaveable { mutableStateOf<String?>(null) }

    // Validaciones: solo se muestran después de pulsar el botón
    val errorNombre = if (nombre.isBlank()) "Ingresa tu nombre" else null
    val errorCorreo =
        if (!Patterns.EMAIL_ADDRESS.matcher(correo.trim()).matches()) "Correo no válido" else null
    val errorTelefono =
        if (telefono.length != 9 || !telefono.all { it.isDigit() }) "Debe tener 9 dígitos" else null
    val errorContrasena = if (contrasena.length < 6) "Mínimo 6 caracteres" else null
    val errorConfirmar = if (confirmar != contrasena) "Las contraseñas no coinciden" else null
    val errorTerminos = if (!acepta) "Debes aceptar los términos y condiciones" else null

    val hayErrores = listOf(
        errorNombre, errorCorreo, errorTelefono, errorContrasena, errorConfirmar, errorTerminos
    ).any { it != null }

    Scaffold(
        topBar = { BarraSuperior("Crear cuenta", onAtras) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CampoTexto(
                valor = nombre,
                onCambio = { nombre = it },
                etiqueta = "Nombre completo",
                error = if (intentoEnviar) errorNombre else null
            )
            CampoTexto(
                valor = correo,
                onCambio = { correo = it; errorRegistro = null },
                etiqueta = "Correo electrónico",
                teclado = KeyboardType.Email,
                error = if (intentoEnviar) errorCorreo else null
            )
            CampoTexto(
                valor = telefono,
                onCambio = { telefono = it },
                etiqueta = "Teléfono",
                teclado = KeyboardType.Phone,
                error = if (intentoEnviar) errorTelefono else null
            )
            CampoTexto(
                valor = contrasena,
                onCambio = { contrasena = it },
                etiqueta = "Contraseña",
                esContrasena = true,
                error = if (intentoEnviar) errorContrasena else null
            )
            CampoTexto(
                valor = confirmar,
                onCambio = { confirmar = it },
                etiqueta = "Confirmar contraseña",
                esContrasena = true,
                error = if (intentoEnviar) errorConfirmar else null
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = acepta, onCheckedChange = { acepta = it })
                Text("Acepto los ", style = MaterialTheme.typography.bodyMedium)
                TextButton(onClick = onVerTerminos) {
                    Text("términos y condiciones")
                }
            }
            if (intentoEnviar && errorTerminos != null) {
                Text(
                    text = errorTerminos,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            if (errorRegistro != null) {
                Text(
                    text = errorRegistro!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(12.dp))

            BotonPrimario(
                texto = "Registrarme",
                onClick = {
                    intentoEnviar = true
                    if (!hayErrores) {
                        val nuevo = Usuario(nombre.trim(), correo.trim(), contrasena, telefono)
                        if (Repositorio.registrarUsuario(nuevo)) {
                            // Inicia sesión directamente con la cuenta recién creada
                            Repositorio.iniciarSesion(nuevo.correo, nuevo.contrasena)
                            onRegistrado()
                        } else {
                            errorRegistro = "Ya existe una cuenta con ese correo"
                        }
                    }
                }
            )
        }
    }
}