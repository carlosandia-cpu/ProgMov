package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.components.cantidadTexto
import com.tecsup.lavacampus.ui.components.fechaCorta
import com.tecsup.lavacampus.ui.components.soles
import com.tecsup.lavacampus.viewmodel.DetallePedidoViewModel

@Composable
fun DetallePedidoScreen(vm: DetallePedidoViewModel, onAtras: () -> Unit) {
    val item by vm.pedido.collectAsState()
    val puedeCancelar by vm.puedeCancelar.collectAsState()
    var confirmando by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = item?.let { "Pedido #${it.pedido.id}" } ?: "Pedido",
                onAtras = onAtras
            )
        }
    ) { padding ->
        val actual = item
        if (actual == null) {
            Box(Modifier.padding(padding).fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Cuerpo(
                item = actual,
                puedeCancelar = puedeCancelar,
                onCancelar = { confirmando = true },
                modifier = Modifier.padding(padding)
            )
        }
    }

    if (confirmando) {
        AlertDialog(
            onDismissRequest = { confirmando = false },
            title = { Text("Cancelar pedido") },
            text = { Text("¿Seguro que quieres cancelar este pedido? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar()
                    confirmando = false
                }) { Text("Sí, cancelar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { confirmando = false }) { Text("No") }
            }
        )
    }
}

@Composable
private fun Cuerpo(
    item: PedidoConDetalle,
    puedeCancelar: Boolean,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pedido = item.pedido
    val estado = EstadoPedido.desde(pedido.estado)
    val casillero = pedido.casillero
    val verde = EstadoPedido.LISTO.color

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cabecera
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Registrado: ${pedido.fechaRegistro.fechaCorta()}")
                Text(
                    "Entrega: ${pedido.fechaEntrega.fechaCorta()}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            EstadoPedidoChip(estado)
        }

        // Seguimiento
        TarjetaSimple {
            Text("Seguimiento", fontWeight = FontWeight.Bold)
            StepperEstado(estado = estado, vertical = true)
        }

        // Líneas
        TarjetaSimple {
            Text("Detalle", fontWeight = FontWeight.Bold)
            item.detalles.forEach { linea ->
                Row(Modifier.fillMaxWidth()) {
                    Column(Modifier.weight(1f)) {
                        Text(linea.servicio.nombre)
                        Text(
                            "${linea.detalle.cantidad.cantidadTexto(linea.servicio.unidad)} × ${linea.detalle.precioUnitario.soles()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(linea.detalle.subtotal.soles())
                }
            }
            HorizontalDivider()
            Row(Modifier.fillMaxWidth()) {
                Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(pedido.total.soles(), fontWeight = FontWeight.Bold)
            }
        }

        // Pago
        TarjetaSimple(color = if (pedido.pagado) verde.copy(alpha = 0.12f) else null) {
            Text("Pago", fontWeight = FontWeight.Bold)
            Text(
                if (pedido.pagado) "Pagado" else "Pago pendiente, se paga al recoger",
                color = if (pedido.pagado) verde else MaterialTheme.colorScheme.onSurface
            )
        }

        // Casillero
        if (estado.esActivo) {
            TarjetaSimple(color = if (estado == EstadoPedido.LISTO) verde.copy(alpha = 0.12f) else null) {
                Text("Casillero", fontWeight = FontWeight.Bold)
                if (estado == EstadoPedido.LISTO && casillero != null) {
                    Text(
                        casillero,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = verde
                    )
                } else {
                    Text("Se asignará al estar listo")
                }
            }
        }

        // Cancelación
        OutlinedButton(
            onClick = onCancelar,
            enabled = puedeCancelar,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cancelar pedido")
        }
        if (!puedeCancelar && estado != EstadoPedido.CANCELADO) {
            Text(
                "Solo se cancela antes de empezar el lavado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TarjetaSimple(
    color: Color? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color ?: MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
    }
}