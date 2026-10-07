package com.saludplus.citas.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior
import com.saludplus.citas.ui.components.BotonPrimario

// Fase 1: nombre del día de la semana de cada fecha fija
private val diasSemanaFijos = mapOf(
    "2026-10-06" to "Mar",
    "2026-10-07" to "Mié",
    "2026-10-08" to "Jue",
    "2026-10-09" to "Vie",
    "2026-10-12" to "Lun"
)

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onAtras: () -> Unit,
    onContinuar: (fecha: String, hora: String) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let { Repositorio.obtenerEspecialidad(it.especialidadId) }

    var fechaSel by rememberSaveable { mutableStateOf<String?>(null) }
    var horaSel by rememberSaveable { mutableStateOf<String?>(null) }

    // Se recalcula sola al cambiar el día o al reservarse una cita
    val horarios = fechaSel
        ?.let { Repositorio.horariosDisponibles(medicoId, it) }
        ?: emptyList()

    val puedeContinuar = fechaSel != null && horaSel != null

    Scaffold(
        topBar = { BarraSuperior("Fecha y hora", onAtras) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Resumen del médico elegido
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = medico?.nombre ?: "Médico",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${especialidad?.nombre ?: ""} · ${medico?.consultorio ?: ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Octubre 2026",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            // Días
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Repositorio.diasFijos.forEach { fecha ->
                    ChipDia(
                        diaSemana = diasSemanaFijos[fecha] ?: "",
                        numero = fecha.substringAfterLast("-").toInt(),
                        seleccionado = fecha == fechaSel,
                        onClick = {
                            fechaSel = fecha
                            horaSel = null // al cambiar de día se reinicia la hora
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))

            // Horarios
            when {
                fechaSel == null -> Text(
                    text = "Elige un día para ver los horarios",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                horarios.isEmpty() -> Text(
                    text = "No hay horarios disponibles para este día",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(horarios, key = { it }) { hora ->
                        ChipHora(
                            hora = hora,
                            seleccionado = hora == horaSel,
                            onClick = { horaSel = hora }
                        )
                    }
                }
            }

            // Empuja el botón hacia abajo cuando no hay grilla
            if (fechaSel == null || horarios.isEmpty()) {
                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(12.dp))

            BotonPrimario(
                texto = "Continuar",
                habilitado = puedeContinuar,
                onClick = {
                    val fecha = fechaSel
                    val hora = horaSel
                    if (fecha != null && hora != null) onContinuar(fecha, hora)
                }
            )
        }
    }
}

@Composable
private fun ChipDia(
    diaSemana: String,
    numero: Int,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorFondo =
        if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val colorTexto =
        if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        border = if (seleccionado) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = diaSemana, style = MaterialTheme.typography.bodySmall, color = colorTexto)
            Text(
                text = numero.toString(),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )
        }
    }
}

@Composable
private fun ChipHora(
    hora: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {
    val colorFondo =
        if (seleccionado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val colorTexto =
        if (seleccionado) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        border = if (seleccionado) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Text(
            text = hora,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = colorTexto,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        )
    }
}