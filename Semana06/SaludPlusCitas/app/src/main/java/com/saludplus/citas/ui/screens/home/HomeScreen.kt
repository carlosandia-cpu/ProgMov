package com.saludplus.citas.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.formatearFecha
import com.saludplus.citas.ui.theme.Coral
import com.saludplus.citas.ui.theme.TealOscuro
import com.saludplus.citas.ui.theme.TealPrimario

@Composable
fun HomeScreen(
    onMenu: () -> Unit,
    onSedes: () -> Unit,
    onDoctores: () -> Unit,
    onAgenda: () -> Unit,
    onSede: (Int) -> Unit
) {
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: "Paciente"
    val destacadas = Repositorio.especialidadesDestacadas()
    val proxima = Repositorio.citasDelUsuario().firstOrNull { it.estado == "Confirmada" }
    val medicoProxima = proxima?.let { Repositorio.obtenerMedico(it.medicoId) }
    val sedeProxima = medicoProxima?.let { Repositorio.obtenerSede(it.sedeId) }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = padding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            // Cabecera con degradado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(TealPrimario, TealOscuro)),
                        RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .statusBarsPadding()
                    .padding(start = 8.dp, end = 20.dp, top = 8.dp, bottom = 64.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onMenu) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        Text(
                            text = "SaludPlus",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text = "Hola, $nombre 👋",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Text(
                            text = "Cuidamos de ti, elige tu sede y agenda en minutos",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            // Todo el contenido sube sobre la cabecera para que la tarjeta quede superpuesta
            Column(modifier = Modifier.offset(y = (-40).dp)) {

                // Próxima cita
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "TU PRÓXIMA CITA",
                            style = MaterialTheme.typography.labelMedium,
                            color = Coral
                        )
                        Spacer(Modifier.height(6.dp))
                        if (proxima != null && medicoProxima != null) {
                            Text(
                                text = medicoProxima.nombre,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "${formatearFecha(proxima.fecha)} · ${proxima.hora}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = sedeProxima?.nombre ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Aún no tienes citas confirmadas",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = "Elige una sede para agendar la primera",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Accesos rápidos
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaAcceso("Sedes", Icons.Default.LocationOn, onSedes, Modifier.weight(1f))
                    TarjetaAcceso("Doctores", Icons.Default.MedicalServices, onDoctores, Modifier.weight(1f))
                    TarjetaAcceso("Agenda", Icons.Default.CalendarMonth, onAgenda, Modifier.weight(1f))
                }

                Spacer(Modifier.height(28.dp))

                // Carrusel de sedes
                Text(
                    text = "Nuestras sedes",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(Repositorio.sedes, key = { it.id }) { sede ->
                        TarjetaSedeCompacta(
                            sede = sede,
                            totalDoctores = Repositorio.medicosPorSede(sede.id).size,
                            onClick = { onSede(sede.id) }
                        )
                    }
                }

                Spacer(Modifier.height(28.dp))

                // Especialidades
                Text(
                    text = "Especialidades destacadas",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(destacadas, key = { it.id }) { especialidad ->
                        TarjetaEspecialidadCompacta(especialidad, onDoctores)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Recordatorio
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Coral.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💡", fontSize = 28.sp)
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Llega 15 minutos antes de tu cita y lleva tu DNI.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun TarjetaAcceso(
    titulo: String,
    icono: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(text = titulo, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun TarjetaSedeCompacta(sede: Sede, totalDoctores: Int, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(230.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(text = sede.nombre, style = MaterialTheme.typography.titleMedium, maxLines = 1)
            Text(
                text = sede.distrito,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = "$totalDoctores doctores",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TarjetaEspecialidadCompacta(
    especialidad: Especialidad,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.width(130.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = especialidad.icono, fontSize = 32.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                text = especialidad.nombre,
                style = MaterialTheme.typography.labelLarge,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}