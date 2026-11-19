
package com.andia.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.andia.centrotareas.model.CargaUi
import com.andia.centrotareas.model.PrimosUi
import com.andia.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    var toques by remember { mutableIntStateOf(0) }
    var numero by rememberSaveable { mutableStateOf("500000") }

    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()

    val calculando = primos is PrimosUi.Calculando
    val cargando = carga is CargaUi.Cargando
    val numeroValido = numero.toIntOrNull()?.let { it >= 2 } == true

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
                        "Ejecución en segundo plano",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { viewModel.cancelar() },
                            enabled = corriendo,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }

                    Text(
                        "Estado: $mensaje",
                        fontWeight = FontWeight.Medium
                    )

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        "${(progreso * 100).toInt()} %",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Contar números primos")

                    Text(
                        "Cálculo pesado en segundo plano",
                        style = MaterialTheme.typography.bodySmall
                    )

                    OutlinedTextField(
                        value = numero,
                        onValueChange = { nuevo ->
                            if (nuevo.all { it.isDigit() }) {
                                numero = nuevo
                            }
                        },
                        label = { Text("Número límite") },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        enabled = !calculando,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                numero.toIntOrNull()?.let {
                                    viewModel.contar(it)
                                }
                            },
                            enabled = !calculando && numeroValido,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.cancelarPrimos()
                            },
                            enabled = calculando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primos) {
                        PrimosUi.Inicial -> {
                            Text("Estado: pendiente")
                        }

                        PrimosUi.Calculando -> {
                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )

                                Text("Calculando números primos...")
                            }
                        }

                        is PrimosUi.Listo -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE3F3E8)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        "Cálculo completado",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF17643C)
                                    )

                                    Text(
                                        "Primos encontrados: ${estado.cantidad}",
                                        color = Color(0xFF17643C)
                                    )

                                    Text(
                                        "Tiempo: ${estado.milisegundos} ms",
                                        color = Color(0xFF17643C)
                                    )
                                }
                            }
                        }

                        PrimosUi.Cancelado -> {
                            Text(
                                "Estado: Cancelado",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TituloTarjeta("Dos cargas")

                    Text(
                        "Comparar ejecución secuencial y paralela",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row(
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.cargarUnaPorUna()
                            },
                            enabled = !cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {
                                viewModel.cargarALaVez()
                            },
                            enabled = !cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("A la vez")
                        }
                    }

                    when (val estado = carga) {
                        CargaUi.Inicial -> {
                            Text("Estado: pendiente")
                        }

                        CargaUi.Cargando -> {
                            Row(
                                horizontalArrangement =
                                    Arrangement.spacedBy(12.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(22.dp),
                                    strokeWidth = 2.dp
                                )

                                Text("Cargando datos...")
                            }
                        }

                        is CargaUi.Exito -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE3F3E8)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement =
                                        Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "Carga completada",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF17643C)
                                    )

                                    Text(
                                        "Modo: ${estado.modo}",
                                        color = Color(0xFF17643C)
                                    )

                                    Text(
                                        "Perfil: ${estado.perfil}",
                                        color = Color(0xFF17643C)
                                    )

                                    Text(
                                        "Pedidos: ${estado.pedidos}",
                                        color = Color(0xFF17643C)
                                    )

                                    Text(
                                        "Tiempo: ${estado.milisegundos} ms",
                                        color = Color(0xFF17643C)
                                    )
                                }
                            }
                        }

                        is CargaUi.Error -> {
                            Text(
                                "Error: ${estado.mensaje}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
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
