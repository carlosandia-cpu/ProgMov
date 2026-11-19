
package com.andia.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andia.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    var toques by remember { mutableIntStateOf(0) }
    val mensaje by viewModel.mensaje.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("CentroTareas", fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Centro de operaciones",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Laboratorio de hilos y tareas en Android",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { toques++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tócame: $toques")
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Tarea con progreso")

                    Text(
                        "Demostración del bloqueo del hilo principal",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Button(
                        onClick = { viewModel.tareaLargaMala() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tarea larga (MAL)")
                    }

                    Text(
                        "Estado: $mensaje",
                        fontWeight = FontWeight.Medium
                    )

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            "Esta tarea bloquea la interfaz durante 4 segundos. " +
                                    "Intenta tocar el contador mientras se ejecuta.",
                            modifier = Modifier.padding(12.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Contar números primos")

                    Text("Cálculo pesado en segundo plano")

                    OutlinedTextField(
                        value = "500000",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Número límite") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(onClick = {}, enabled = false) {
                            Text("Contar")
                        }

                        OutlinedButton(onClick = {}, enabled = false) {
                            Text("Cancelar")
                        }
                    }

                    Text("Estado: pendiente")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Dos cargas")

                    Text("Comparar ejecución secuencial y paralela")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(onClick = {}, enabled = false) {
                            Text("Una por una")
                        }

                        Button(onClick = {}, enabled = false) {
                            Text("A la vez")
                        }
                    }

                    Text("Estado: pendiente")
                }
            }
        }
    }
}

@Composable
private fun TituloTarjeta(titulo: String) {
    Text(
        text = titulo,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}
