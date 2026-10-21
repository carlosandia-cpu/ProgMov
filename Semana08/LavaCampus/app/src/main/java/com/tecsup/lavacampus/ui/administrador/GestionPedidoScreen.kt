package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Casilleros
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.components.cantidadTexto
import com.tecsup.lavacampus.ui.components.soles
import com.tecsup.lavacampus.viewmodel.GestionPedidoViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GestionPedidoScreen(vm: GestionPedidoViewModel, onAtras: () -> Unit) {
    val item by vm.pedido.collectAsState()
    val ocupados by vm.casillerosOcupados.collectAsState()
    val elegido by vm.casilleroElegido.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = { AppTopBar(titulo = item?.let { "Pedido #${it.pedido.id}" } ?: "Pedido", onAtras = onAtras) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { padding ->
        val actual = item
        if (actual == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val pedido = actual.pedido
            val estado = EstadoPedido.desde(pedido.estado)
            val siguiente = estado.siguiente()
            val puedeCancelar = estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO

            Column(
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Tarjeta {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(actual.usuario.nombre, fontWeight = FontWeight.Bold)
                            Text(
                                "${actual.usuario.codigo} · ${actual.usuario.correo}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        EstadoPedidoChip(estado)
                    }
                }

                Tarjeta {
                    Text("Seguimiento", fontWeight = FontWeight.Bold)
                    StepperEstado(estado = estado, vertical = true)
                }

                if (estado == EstadoPedido.EN_PROCESO) {
                    Tarjeta {
                        Text("Casillero", fontWeight = FontWeight.Bold)
                        Text(
                            elegido?.let { "Elegido: $it" } ?: "Elige un casillero libre para marcar como listo",
                            style = MaterialTheme.typography.bodySmall
                        )
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Casilleros.todos.forEach { c ->
                                FilterChip(
                                    selected = elegido == c,
                                    onClick = { vm.elegirCasillero(c) },
                                    enabled = c !in ocupados,
                                    label = { Text(c) }
                                )
                            }
                        }
                    }
                } else if (pedido.casillero != null && estado != EstadoPedido.CANCELADO) {
                    Tarjeta {
                        Text("Casillero", fontWeight = FontWeight.Bold)
                        Text(
                            pedido.casillero,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = EstadoPedido.LISTO.color
                        )
                    }
                }

                Tarjeta {
                    Text("Detalle", fontWeight = FontWeight.Bold)
                    actual.detalles.forEach { linea ->
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                "${linea.servicio.nombre} (${linea.detalle.cantidad.cantidadTexto(linea.servicio.unidad)})",
                                modifier = Modifier.weight(1f)
                            )
                            Text(linea.detalle.subtotal.soles())
                        }
                    }
                    HorizontalDivider()
                    Row(Modifier.fillMaxWidth()) {
                        Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text(pedido.total.soles(), fontWeight = FontWeight.Bold)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Cobrado", modifier = Modifier.weight(1f))
                        Switch(
                            checked = pedido.pagado,
                            onCheckedChange = vm::marcarPagado,
                            enabled = estado.esActivo
                        )
                    }
                }

                Text(
                    "Regla: para pasar a Listo elige un casillero libre. Para entregar, el pedido debe estar cobrado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (siguiente != null) {
                    LavaButton(texto = "Marcar como ${siguiente.etiqueta}", onClick = vm::avanzar)
                }
                OutlinedButton(
                    onClick = vm::cancelar,
                    enabled = puedeCancelar,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Cancelar pedido")
                }
            }
        }
    }
}

@Composable
private fun Tarjeta(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
    }
}