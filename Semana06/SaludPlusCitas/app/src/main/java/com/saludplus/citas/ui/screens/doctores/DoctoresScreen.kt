package com.saludplus.citas.ui.screens.doctores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BarraSuperior

/** Doctores agrupados por especialidad: Especialidad, Código, Sede y Teléfono. */
@Composable
fun DoctoresScreen(onMenu: () -> Unit, onMedico: (Int) -> Unit) {
    var busqueda by rememberSaveable { mutableStateOf("") }
    val grupos = Repositorio.doctoresPorEspecialidad(texto = busqueda)

    Scaffold(
        topBar = { BarraSuperior("Doctores", onMenu = onMenu) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                placeholder = { Text("Buscar doctor") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            if (grupos.isEmpty()) {
                Text(
                    text = "No se encontraron doctores",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(24.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    grupos.forEach { (esp, lista) ->
                        item(key = "esp_${esp.id}") {
                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = esp.icono, fontSize = 22.sp)
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "${esp.nombre} (${lista.size})",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        items(lista, key = { "doc_${it.id}" }) { medico ->
                            TarjetaDoctor(
                                medico = medico,
                                especialidad = esp.nombre,
                                onClick = { onMedico(medico.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaDoctor(medico: Medico, especialidad: String, onClick: () -> Unit) {
    val sede = Repositorio.obtenerSede(medico.sedeId)

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = medico.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            DatoDoctor("Especialidad", especialidad)
            DatoDoctor("Código", medico.codigo)
            DatoDoctor("Sede", sede?.nombre ?: "-")
            DatoDoctor("Teléfono", medico.telefono)
        }
    }
}

@Composable
private fun DatoDoctor(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.labelLarge
        )
    }
}