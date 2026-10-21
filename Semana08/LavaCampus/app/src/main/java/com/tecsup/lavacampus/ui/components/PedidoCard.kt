package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido

@Composable
fun PedidoCard(
    item: PedidoConDetalle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCancelar: (() -> Unit)? = null
) {
    val pedido = item.pedido
    val estado = EstadoPedido.desde(pedido.estado)
    val resumen = item.detalles.joinToString(" · ") { it.servicio.nombre }
    val icono = item.detalles.firstOrNull()?.servicio?.icono ?: "wash"
    val casillero = pedido.casillero

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (estado == EstadoPedido.LISTO) estado.color.copy(alpha = 0.12f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    IconoServicio(icono = icono, tamano = 28.dp)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Pedido #${pedido.id}", fontWeight = FontWeight.Bold)
                    Text(
                        resumen,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "Entrega: ${pedido.fechaEntrega.fechaCorta()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(pedido.total.soles(), fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(4.dp))
                    EstadoPedidoChip(estado)
                }
            }
            if (estado == EstadoPedido.LISTO && casillero != null) {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Recógelo en el casillero $casillero",
                    color = estado.color,
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (onCancelar != null && estado == EstadoPedido.RECIBIDO) {
                TextButton(onClick = onCancelar) {
                    Text("Cancelar", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}