package com.andia.centrotareas.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andia.centrotareas.model.CargaUi
import com.andia.centrotareas.model.PrimosUi
import com.andia.centrotareas.ui.theme.*
import com.andia.centrotareas.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {
    var toques by rememberSaveable { mutableIntStateOf(0) }
    var numero by rememberSaveable { mutableStateOf("500000") }

    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()

    val calculando = primos is PrimosUi.Calculando
    val cargando = carga is CargaUi.Cargando
    val numeroValido = numero.toIntOrNull()?.let { it >= 2 } == true
    val ocupado = corriendo || calculando || cargando

    val progresoAnimado by animateFloatAsState(
        targetValue = progreso,
        animationSpec = tween(durationMillis = 120),
        label = "ProgresoTarea"
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "CentroTareas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulNoche,
                    titleContentColor = Blanco
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
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            PanelBienvenida(
                toques = toques,
                ocupado = ocupado,
                onToque = { toques++ }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Herramientas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "3 módulos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            TarjetaModulo(
                titulo = "Progreso de tareas",
                subtitulo = "Ejecución y cancelación"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Estado: $mensaje", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${(progreso * 100).toInt()} %",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                LinearProgressIndicator(
                    progress = { progresoAnimado },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(9.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = Turquesa,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BotonPrincipal(
                        texto = "Iniciar",
                        habilitado = !corriendo,
                        accion = { viewModel.tareaLarga() },
                        modifier = Modifier.weight(1f)
                    )
                    BotonSecundario(
                        texto = "Cancelar",
                        habilitado = corriendo,
                        accion = { viewModel.cancelar() },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            TarjetaModulo(
                titulo = "Números primos",
                subtitulo = "Cálculos intensivos de CPU"
            ) {
                OutlinedTextField(
                    value = numero,
                    onValueChange = { nuevo ->
                        if (nuevo.all { it.isDigit() }) numero = nuevo
                    },
                    label = { Text("Número límite") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = !calculando,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BotonPrincipal(
                        texto = "Contar",
                        habilitado = !calculando && numeroValido,
                        accion = {
                            numero.toIntOrNull()?.let { viewModel.contar(it) }
                        },
                        modifier = Modifier.weight(1f)
                    )
                    BotonSecundario(
                        texto = "Cancelar",
                        habilitado = calculando,
                        accion = { viewModel.cancelarPrimos() },
                        modifier = Modifier.weight(1f)
                    )
                }

                when (val estado = primos) {
                    PrimosUi.Inicial -> TextoSecundario("Estado: pendiente")
                    PrimosUi.Calculando -> IndicadorCarga("Calculando números primos...")
                    is PrimosUi.Listo -> {
                        PanelResultado(titulo = "Cálculo completado", exito = true) {
                            Text(
                                "Primos encontrados: ${estado.cantidad}",
                                color = TextoExito,
                                fontWeight = FontWeight.Medium
                            )
                            Text("Tiempo: ${estado.milisegundos} ms", color = TextoExito)
                        }
                    }
                    PrimosUi.Cancelado -> {
                        Text(
                            "Estado: Cancelado",
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            TarjetaModulo(
                titulo = "Dos cargas",
                subtitulo = "Comparación de rendimiento"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Simular error", fontWeight = FontWeight.Medium)
                        TextoSecundario("Prueba la recuperación de datos")
                    }
                    Switch(
                        checked = simularError,
                        onCheckedChange = { viewModel.cambiarSimularError(it) },
                        enabled = !cargando,
                        colors = SwitchDefaults.colors(checkedTrackColor = Turquesa)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BotonSecundario(
                        texto = "Una por una",
                        habilitado = !cargando,
                        accion = { viewModel.cargarUnaPorUna() },
                        modifier = Modifier.weight(1f)
                    )
                    BotonPrincipal(
                        texto = "A la vez",
                        habilitado = !cargando,
                        accion = { viewModel.cargarALaVez() },
                        modifier = Modifier.weight(1f)
                    )
                }

                when (val estado = carga) {
                    CargaUi.Inicial -> TextoSecundario("Estado: pendiente")
                    CargaUi.Cargando -> IndicadorCarga("Cargando información...")
                    is CargaUi.Exito -> {
                        PanelResultado(titulo = "Carga completada", exito = true) {
                            Text("Modo: ${estado.modo}", color = TextoExito)
                            Text("Perfil: ${estado.perfil}", color = TextoExito)
                            Text("Pedidos: ${estado.pedidos}", color = TextoExito)
                            Text("Tiempo: ${estado.milisegundos} ms", color = TextoExito)
                        }
                    }
                    is CargaUi.Error -> {
                        PanelResultado(titulo = "Error en la carga", exito = false) {
                            Text(estado.mensaje, color = TextoError)
                            OutlinedButton(
                                onClick = { viewModel.cargarALaVez() },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = TextoError
                                )
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }

            Text(
                "CENTROTAREAS · LABORATORIO 14",
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun PanelBienvenida(
    toques: Int,
    ocupado: Boolean,
    onToque: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    listOf(AzulNoche, Color(0xFF244B6B))
                )
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "PANEL DE OPERACIONES",
            color = Color(0xFF55E1C4),
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
        )
        Text(
            "Todo bajo control.",
            color = Blanco,
            fontWeight = FontWeight.Bold,
            fontSize = 23.sp
        )
        Text(
            "Gestiona procesos sin detener tu aplicación.",
            color = Color(0xFFCFDCE8),
            style = MaterialTheme.typography.bodySmall
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.09f))
                .padding(13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Estado del sistema", color = Blanco, fontSize = 13.sp)
            Text(
                if (ocupado) "Procesando" else "Listo",
                color = Color(0xFF55E1C4),
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        }
        Button(
            onClick = onToque,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blanco,
                contentColor = AzulNoche
            )
        ) {
            Text("Tócame: $toques", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TarjetaModulo(
    titulo: String,
    subtitulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    subtitulo,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)
            )
            contenido()
        }
    }
}

@Composable
private fun BotonPrincipal(
    texto: String,
    habilitado: Boolean,
    accion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = accion,
        enabled = habilitado,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulPrincipal,
            contentColor = Blanco
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 12.dp)
    ) {
        Text(
            texto,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BotonSecundario(
    texto: String,
    habilitado: Boolean,
    accion: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = accion,
        enabled = habilitado,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)
    ) {
        Text(
            texto,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun IndicadorCarga(texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(22.dp),
            strokeWidth = 2.dp,
            color = Turquesa
        )
        Text(texto, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
private fun TextoSecundario(texto: String) {
    Text(
        texto,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun PanelResultado(
    titulo: String,
    exito: Boolean,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (exito) FondoExito else FondoError)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            titulo,
            color = if (exito) TextoExito else TextoError,
            fontWeight = FontWeight.Bold
        )
        contenido()
    }
}
