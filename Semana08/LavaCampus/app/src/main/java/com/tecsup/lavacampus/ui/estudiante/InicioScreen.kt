package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.ui.components.StepperEstado
import com.tecsup.lavacampus.ui.components.fechaCorta
import com.tecsup.lavacampus.viewmodel.InicioViewModel

@Composable
fun InicioScreen(
    usuario: Usuario,
    vm: InicioViewModel,
    modifier: Modifier = Modifier,
    onNuevoPedido: () -> Unit,
    onMisPedidos: () -> Unit,
    onTarifas: () -> Unit,
    onDetalle: (Int) -> Unit
) {
    val activo by vm.pedidoActivo.collectAsState()
    val recientes by vm.recientes.collectAsState()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    "Hola, ${usuario.nombre.substringBefore(" ")}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Código ${usuario.codigo}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item { TarjetaActiva(activo, onDetalle) }
        item { LavaButton(texto = "Nuevo pedido", onClick = onNuevoPedido) }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(onClick = onMisPedidos, modifier = Modifier.weight(1f)) {
                    Text("Mis pedidos")
                }
                OutlinedButton(onClick = onTarifas, modifier = Modifier.weight(1f)) {
                    Text("Tarifas")
                }
            }
        }
        item {
            Text(
                "Últimos pedidos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (recientes.isEmpty()) {
            item {
                Text(
                    "Aún no tienes pedidos",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(recientes, key = { it.pedido.id }) { pedido ->
                PedidoCard(item = pedido, onClick = { onDetalle(pedido.pedido.id) })
            }
        }
    }
}

@Composable
private fun TarjetaActiva(item: PedidoConDetalle?, onDetalle: (Int) -> Unit) {
    val colores = MaterialTheme.colorScheme
    Card(
        onClick = { item?.let { onDetalle(it.pedido.id) } },
        enabled = item != null,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colores.primary,
            contentColor = colores.onPrimary,
            disabledContainerColor = colores.primary,
            disabledContentColor = colores.onPrimary
        )
    ) {
        Column(Modifier.padding(18.dp)) {
            if (item == null) {
                Text("Mi pedido activo", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    "No tienes pedidos activos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                val pedido = item.pedido
                val estado = EstadoPedido.desde(pedido.estado)
                Text(
                    "Pedido #${pedido.id}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(14.dp))
                StepperEstado(
                    estado = estado,
                    colorActivo = colores.onPrimary,
                    colorInactivo = colores.onPrimary.copy(alpha = 0.35f),
                    colorMarca = colores.primary,
                    colorTexto = colores.onPrimary
                )
                Spacer(Modifier.height(14.dp))
                Text("Entrega estimada: ${pedido.fechaEntrega.fechaCorta()}")
                val casillero = pedido.casillero
                if (estado == EstadoPedido.LISTO && casillero != null) {
                    Spacer(Modifier.height(10.dp))
                    Surface(
                        color = colores.onPrimary.copy(alpha = 0.2f),
                        contentColor = colores.onPrimary,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Recógelo en el casillero $casillero",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}