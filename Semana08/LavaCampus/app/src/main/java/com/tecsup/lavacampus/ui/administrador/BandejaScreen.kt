package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.EstadoPedidoChip
import com.tecsup.lavacampus.ui.components.cantidadTexto
import com.tecsup.lavacampus.ui.components.fechaCorta
import com.tecsup.lavacampus.ui.components.soles
import com.tecsup.lavacampus.viewmodel.BandejaViewModel
import com.tecsup.lavacampus.viewmodel.FiltroBandeja

@Composable
fun BandejaScreen(
    vm: BandejaViewModel,
    modifier: Modifier = Modifier,
    onGestion: (Int) -> Unit
) {
    val pedidos by vm.pedidos.collectAsState()
    val filtro by vm.filtro.collectAsState()
    val busqueda by vm.busqueda.collectAsState()
    val idsAtrasados by vm.idsAtrasados.collectAsState()

    Column(modifier.fillMaxSize()) {
        OutlinedTextField(
            value = busqueda,
            onValueChange = vm::buscar,
            label = { Text("Buscar por estudiante o número") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 8.dp)
        )
        Row(
            Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FiltroBandeja.entries.forEach { f ->
                FilterChip(
                    selected = filtro == f,
                    onClick = { vm.elegirFiltro(f) },
                    label = { Text(f.etiqueta) }
                )
            }
        }

        if (pedidos.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No hay pedidos con ese filtro", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pedidos, key = { it.pedido.id }) { item ->
                    TarjetaBandeja(
                        item = item,
                        esAtrasado = item.pedido.id in idsAtrasados,
                        onAbrir = { onGestion(item.pedido.id) },
                        onIniciar = { vm.iniciar(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaBandeja(
    item: PedidoConDetalle,
    esAtrasado: Boolean,
    onAbrir: () -> Unit,
    onIniciar: () -> Unit
) {
    val pedido = item.pedido
    val estado = EstadoPedido.desde(pedido.estado)
    val detalle = item.detalles.joinToString(" · ") {
        "${it.servicio.nombre} ${it.detalle.cantidad.cantidadTexto(it.servicio.unidad)}"
    }

    Card(
        onClick = onAbrir,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (esAtrasado) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
            else MaterialTheme.colorScheme.surface
        ),
        border = if (esAtrasado) BorderStroke(1.5.dp, MaterialTheme.colorScheme.error) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Pedido #${pedido.id} · ${item.usuario.nombre}", fontWeight = FontWeight.Bold)
                        if (esAtrasado) {
                            Text(
                                "Atrasado",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        detalle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Entrega: ${pedido.fechaEntrega.fechaCorta()}" +
                                (pedido.casillero?.let { " · Casillero $it" } ?: ""),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(pedido.total.soles(), fontWeight = FontWeight.Bold)
                    EstadoPedidoChip(estado)
                    if (pedido.pagado) {
                        Text("Cobrado", style = MaterialTheme.typography.labelSmall, color = EstadoPedido.LISTO.color)
                    }
                }
            }
            when (estado) {
                EstadoPedido.RECIBIDO -> FilledTonalButton(onClick = onIniciar) { Text("Iniciar") }
                EstadoPedido.EN_PROCESO -> FilledTonalButton(onClick = onAbrir) { Text("Marcar listo") }
                EstadoPedido.LISTO -> FilledTonalButton(onClick = onAbrir) { Text("Entregar") }
                else -> Unit
            }
        }
    }
}