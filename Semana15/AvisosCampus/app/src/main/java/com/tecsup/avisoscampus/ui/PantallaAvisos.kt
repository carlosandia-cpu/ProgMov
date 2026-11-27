package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: manejar permiso denegado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TarjetaAvisos("Permiso de notificaciones") {

                Text(
                    text = "Sin pedir",
                    color = Color.Gray
                )

                // TODO Paso 2: solicitar permiso

                Button(onClick = {}) {
                    Text("Permitir notificaciones")
                }
            }

            TarjetaAvisos("Avisos") {

                // TODO Paso 3: mostrar aviso

                Button(onClick = {}) {
                    Text("Mostrar aviso")
                }
            }

            TarjetaAvisos("Recordatorio") {

                // TODO Paso 5: cuenta regresiva

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(onClick = {}) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = {},
                        enabled = false
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            TarjetaAvisos("Receptor de anuncios") {

                // TODO Paso 6: escuchar modo avion

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Escuchar el modo avión")

                    Switch(
                        checked = false,
                        onCheckedChange = {}
                    )
                }
            }

            TarjetaAvisos("Historial") {

                // TODO Paso 7: mostrar historial

                Text("Sin avisos todavía")
            }
        }
    }
}

@Composable
fun TarjetaAvisos(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            contenido()
        }
    }
}