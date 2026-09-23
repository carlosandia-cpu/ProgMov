package com.example.tecsupfit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tecsupfit.data.obtenerHorarios
import com.example.tecsupfit.model.ClaseGimnasio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    clase: ClaseGimnasio,
    onVolver: () -> Unit,
    onReservar: (Int) -> Unit
) {
    val horarios = obtenerHorarios(clase)
    var horarioSeleccionado by rememberSaveable {
        mutableStateOf<Int?>(null)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Detalle de clase")
                },
                navigationIcon = {
                    Button(onClick = onVolver) {
                        Text("← Volver")
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = clase.nombre,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Text("${clase.horario} · ${clase.sala} · ${clase.duracion}")

            Text(clase.descripcion)

            Text(
                text = "${clase.cuposDisponibles} cupos disponibles",
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Selecciona un horario",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            horarios.forEachIndexed { indice, horario ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = horarioSeleccionado == indice,
                            onClick = {
                                horarioSeleccionado = indice
                            },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = horarioSeleccionado == indice,
                        onClick = null
                    )

                    Text(
                        text = horario,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    horarioSeleccionado?.let { indice ->
                        onReservar(indice)
                    }
                },
                enabled = horarioSeleccionado != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Reservar cupo")
            }
        }
    }
}