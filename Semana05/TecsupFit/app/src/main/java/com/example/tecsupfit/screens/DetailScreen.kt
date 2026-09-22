package com.example.tecsupfit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tecsupfit.model.ClaseGimnasio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    clase: ClaseGimnasio,
    onBack: () -> Unit,
    onReservar: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Detalle de clase")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(text = "← Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = clase.nombre,
                fontWeight = FontWeight.Bold
            )

            HorizontalDivider()

            Text(text = "Horario: ${clase.horario}")
            Text(text = "Sala: ${clase.sala}")
            Text(text = "Duración: ${clase.duracion}")
            Text(text = "Cupos disponibles: ${clase.cuposDisponibles}")

            Text(
                text = "Descripción",
                fontWeight = FontWeight.Bold
            )

            Text(text = clase.descripcion)

            Button(
                onClick = onReservar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Reservar cupo")
            }
        }
    }
}