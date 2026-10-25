package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.RegistroViewModel

@Composable
fun RegistroScreen(
    vm: RegistroViewModel,
    onAtras: () -> Unit,
    onRegistrado: () -> Unit
) {
    val error by vm.error.collectAsState()
    val guardando by vm.guardando.collectAsState()

    var nombre by rememberSaveable { mutableStateOf("") }
    var codigo by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var repetir by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(titulo = "Crear cuenta", onAtras = onAtras) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Registra tu cuenta de estudiante. Después podrás iniciar sesión.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            LavaTextField(
                valor = nombre,
                onCambio = { nombre = it; vm.limpiarError() },
                etiqueta = "Nombre completo",
                icono = Icons.Default.Person,
                hayError = error != null
            )
            LavaTextField(
                valor = codigo,
                onCambio = { codigo = it; vm.limpiarError() },
                etiqueta = "Código de estudiante (ej. U202310234)",
                icono = Icons.Default.Badge,
                hayError = error != null
            )
            LavaTextField(
                valor = correo,
                onCambio = { correo = it; vm.limpiarError() },
                etiqueta = "Correo",
                icono = Icons.Default.Email,
                teclado = KeyboardType.Email,
                hayError = error != null
            )
            LavaTextField(
                valor = clave,
                onCambio = { clave = it; vm.limpiarError() },
                etiqueta = "Contraseña (mínimo 4 caracteres)",
                icono = Icons.Default.Lock,
                esClave = true,
                hayError = error != null
            )
            LavaTextField(
                valor = repetir,
                onCambio = { repetir = it; vm.limpiarError() },
                etiqueta = "Repite la contraseña",
                icono = Icons.Default.Lock,
                esClave = true,
                hayError = error != null
            )

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            LavaButton(
                texto = "Crear cuenta",
                onClick = { vm.registrar(nombre, codigo, correo, clave, repetir, onRegistrado) },
                enabled = !guardando
            )
            TextButton(onClick = onAtras, modifier = Modifier.fillMaxWidth()) {
                Text("Ya tengo cuenta")
            }
        }
    }
}