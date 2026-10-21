package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.SelectorCantidad
import com.tecsup.lavacampus.ui.components.cantidadTexto
import com.tecsup.lavacampus.ui.components.fechaCorta
import com.tecsup.lavacampus.ui.components.soles
import com.tecsup.lavacampus.ui.components.textoPrecio
import com.tecsup.lavacampus.viewmodel.NuevoPedidoViewModel

@Composable
fun NuevoPedidoScreen(
    vm: NuevoPedidoViewModel,
    onAtras: () -> Unit,
    onCreado: () -> Unit
) {
    val servicios by vm.servicios.collectAsState()
    val cantidades by vm.cantidades.collectAsState()
    val resumen by vm.resumen.collectAsState()
    val total by vm.total.collectAsState()
    val fechaEntrega by vm.fechaEntrega.collectAsState()
    val nota by vm.nota.collectAsState()
    val mensaje by vm.mensaje.collectAsState()
    val recibir by vm.recibirPedidos.collectAsState()
    val snackbar = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let {
            snackbar.showSnackbar(it)
            vm.limpiarMensaje()
        }
    }

    Scaffold(
        topBar = { AppTopBar(titulo = "Nuevo pedido", onAtras = onAtras) },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                Box(Modifier.navigationBarsPadding().padding(16.dp)) {
                    LavaButton(
                        texto = "Confirmar pedido",
                        onClick = { vm.confirmar(onCreado) },
                        enabled = resumen.isNotEmpty() && recibir
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (!recibir) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    ) {
                        Text(
                            "La lavandería no está recibiendo pedidos",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
            item {
                Text(
                    "Elige los servicios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
            items(servicios, key = { it.id }) { servicio ->
                FilaServicio(
                    servicio = servicio,
                    cantidad = cantidades[servicio.id] ?: 0.0,
                    onDelta = { delta -> vm.cambiarCantidad(servicio.id, delta) }
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Resumen", fontWeight = FontWeight.Bold)
                        if (resumen.isEmpty()) {
                            Text("Aún no eliges servicios")
                        } else {
                            resumen.forEach { linea ->
                                Row(Modifier.fillMaxWidth()) {
                                    Text(
                                        "${linea.servicio.nombre} (${linea.cantidad.cantidadTexto(linea.servicio.unidad)})",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(linea.subtotal.soles())
                                }
                            }
                            HorizontalDivider()
                            Row(Modifier.fillMaxWidth()) {
                                Text("Total", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                                Text(total.soles(), fontWeight = FontWeight.Bold)
                            }
                            fechaEntrega?.let {
                                Text("Entrega estimada: ${it.fechaCorta()}")
                            }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = nota,
                    onValueChange = vm::actualizarNota,
                    label = { Text("Nota (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                )
            }
        }
    }
}

@Composable
private fun FilaServicio(
    servicio: Servicio,
    cantidad: Double,
    onDelta: (Double) -> Unit
) {
    val unidad = Unidad.desde(servicio.unidad)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconoServicio(icono = servicio.icono)
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(servicio.nombre, fontWeight = FontWeight.Bold)
                    Text(
                        "${servicio.textoPrecio()} · Entrega en ${servicio.horasEntrega} h",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Cantidad (${unidad.etiqueta.lowercase()})",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                SelectorCantidad(
                    valor = cantidad,
                    paso = unidad.paso,
                    minimo = unidad.minimo,
                    maximo = unidad.maximo,
                    onDelta = onDelta
                )
            }
        }
    }
}