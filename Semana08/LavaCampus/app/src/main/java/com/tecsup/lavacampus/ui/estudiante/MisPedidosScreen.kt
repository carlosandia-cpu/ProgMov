package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.ui.components.PedidoCard
import com.tecsup.lavacampus.viewmodel.MisPedidosViewModel

@Composable
fun MisPedidosScreen(
    vm: MisPedidosViewModel,
    modifier: Modifier = Modifier,
    onDetalle: (Int) -> Unit
) {
    val activos by vm.activos.collectAsState()
    val historial by vm.historial.collectAsState()
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    var porCancelar by remember { mutableStateOf<PedidoConDetalle?>(null) }

    Column(modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pestana) {
            Tab(
                selected = pestana == 0,
                onClick = { pestana = 0 },
                text = { Text("Activos (${activos.size})") }
            )
            Tab(
                selected = pestana == 1,
                onClick = { pestana = 1 },
                text = { Text("Historial (${historial.size})") }
            )
        }

        val lista = if (pestana == 0) activos else historial
        if (lista.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    if (pestana == 0) "No tienes pedidos activos" else "Aún no hay pedidos en el historial",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(lista, key = { it.pedido.id }) { pedido ->
                    PedidoCard(
                        item = pedido,
                        onClick = { onDetalle(pedido.pedido.id) },
                        onCancelar = if (pestana == 0) {
                            { porCancelar = pedido }
                        } else null
                    )
                }
            }
        }
    }

    porCancelar?.let { pedido ->
        AlertDialog(
            onDismissRequest = { porCancelar = null },
            title = { Text("Cancelar pedido") },
            text = { Text("¿Seguro que quieres cancelar el pedido #${pedido.pedido.id}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.cancelar(pedido)
                    porCancelar = null
                }) { Text("Sí, cancelar", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { porCancelar = null }) { Text("No") }
            }
        )
    }
}