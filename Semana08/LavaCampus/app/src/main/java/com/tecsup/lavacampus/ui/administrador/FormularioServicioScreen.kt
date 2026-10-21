package com.tecsup.lavacampus.ui.administrador

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad
import com.tecsup.lavacampus.ui.components.AppTopBar
import com.tecsup.lavacampus.ui.components.FilaSwitch
import com.tecsup.lavacampus.ui.components.IconoServicio
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.viewmodel.ServiciosViewModel
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormularioServicioScreen(vm: ServiciosViewModel, id: Int, onAtras: () -> Unit) {
    val servicios by vm.servicios.collectAsState()
    val existente = remember(servicios, id) { servicios.firstOrNull { it.id == id } }

    var cargado by rememberSaveable { mutableStateOf(id == 0) }
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var unidad by rememberSaveable { mutableStateOf(Unidad.KG.name) }
    var precioTexto by rememberSaveable { mutableStateOf("") }
    var horas by rememberSaveable { mutableIntStateOf(24) }
    var icono by rememberSaveable { mutableStateOf("wash") }
    var activo by rememberSaveable { mutableStateOf(true) }
    var intentado by rememberSaveable { mutableStateOf(false) }

    // Al editar, carga los datos del servicio una sola vez
    LaunchedEffect(existente) {
        val s = existente
        if (!cargado && s != null) {
            nombre = s.nombre
            descripcion = s.descripcion
            unidad = s.unidad
            precioTexto = String.format(Locale.US, "%.2f", s.precio)
            horas = s.horasEntrega
            icono = s.icono
            activo = s.activo
            cargado = true
        }
    }

    val precio = precioTexto.replace(',', '.').toDoubleOrNull()
    val errorNombre = intentado && nombre.isBlank()
    val errorPrecio = intentado && (precio == null || precio <= 0.0)

    Scaffold(
        topBar = {
            AppTopBar(
                titulo = if (id == 0) "Nuevo servicio" else "Editar servicio",
                onAtras = onAtras
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            LavaTextField(
                valor = nombre,
                onCambio = { nombre = it },
                etiqueta = "Nombre del servicio",
                hayError = errorNombre
            )
            if (errorNombre) Aviso("El nombre es obligatorio")

            LavaTextField(
                valor = descripcion,
                onCambio = { descripcion = it },
                etiqueta = "Descripción"
            )

            Text("Se cobra por", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Unidad.entries.forEach { u ->
                    FilterChip(
                        selected = unidad == u.name,
                        onClick = { unidad = u.name },
                        label = { Text(u.etiqueta) }
                    )
                }
            }

            LavaTextField(
                valor = precioTexto,
                onCambio = { precioTexto = it },
                etiqueta = "Precio (S/)",
                teclado = KeyboardType.Decimal,
                hayError = errorPrecio
            )
            if (errorPrecio) Aviso("El precio debe ser mayor que 0")

            Text("Tiempo de entrega", fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(24, 48, 72).forEach { h ->
                    FilterChip(
                        selected = horas == h,
                        onClick = { horas = h },
                        label = { Text("$h h") }
                    )
                }
            }

            Text("Ícono", fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconoServicio(icono = icono, tamano = 48.dp)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("wash" to "Lavadora", "iron" to "Plancha", "shirt" to "Camisa").forEach { (clave, texto) ->
                        FilterChip(
                            selected = icono == clave,
                            onClick = { icono = clave },
                            label = { Text(texto) }
                        )
                    }
                }
            }

            FilaSwitch(
                titulo = "Servicio activo",
                descripcion = "Si está inactivo no aparece al hacer un pedido",
                marcado = activo,
                onCambio = { activo = it }
            )

            LavaButton(
                texto = "Guardar servicio",
                onClick = {
                    intentado = true
                    if (nombre.isNotBlank() && precio != null && precio > 0.0) {
                        vm.guardar(
                            Servicio(
                                id = id,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim(),
                                unidad = unidad,
                                precio = precio,
                                horasEntrega = horas,
                                activo = activo,
                                icono = icono
                            )
                        ) { onAtras() }
                    }
                }
            )
        }
    }
}

@Composable
private fun Aviso(texto: String) {
    Text(texto, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
}