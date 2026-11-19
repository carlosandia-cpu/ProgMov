
package com.andia.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.andia.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(
    viewModel: TareasViewModel
) {
    var toques by remember { mutableIntStateOf(0) }

    val mensaje by viewModel.mensaje.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CentroTareas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary
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
                "Ejecuta tareas y observa sus estados.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Button(
                onClick = { toques++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tócame: $toques")
            }

            // Tarjeta 1: tarea con progreso
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Tarea con progreso")

                    Text(
                        "Estado: $mensaje",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("Cancelar")
                        }
                    }

                    LinearProgressIndicator(
                        progress = { 0f },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("0 %")
                }
            }

            // Tarjeta 2: contar números primos
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Contar números primos")

                    Text(
                        "Cálculo pesado en segundo plano"
                    )

                    OutlinedTextField(
                        value = "500000",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Número límite") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("Cancelar")
                        }
                    }

                    Text("Estado: pendiente")
                }
            }

            // Tarjeta 3: dos cargas
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Dos cargas")

                    Text(
                        "Comparar ejecución secuencial y paralela"
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {},
                            enabled = false
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {},
                            enabled = false
                        ) {
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
