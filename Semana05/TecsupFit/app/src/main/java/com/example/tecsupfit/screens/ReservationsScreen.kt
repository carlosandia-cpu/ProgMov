package com.example.tecsupfit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tecsupfit.model.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationsScreen(
    reservas: List<Reserva>,
    onCancelarReserva: (Reserva) -> Unit = {}
) {
    // Reserva que el usuario quiere cancelar; distinta de null muestra el AlertDialog.
    var reservaACancelar by remember { mutableStateOf<Reserva?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis reservas",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { paddingValues ->

        if (reservas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Todavía no tienes reservas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Selecciona una clase desde Inicio para reservar un cupo.",
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(reservas) { reserva ->
                    ReservaCard(
                        reserva = reserva,
                        onCancelarClick = { reservaACancelar = reserva }
                    )
                }
            }
        }

        // AlertDialog de confirmación antes de cancelar la reserva.
        reservaACancelar?.let { reserva ->
            AlertDialog(
                onDismissRequest = { reservaACancelar = null },
                title = { Text(text = "Cancelar reserva") },
                text = {
                    Text(
                        text = "¿Seguro que deseas cancelar tu reserva de " +
                                "\"${reserva.clase.nombre}\" a las ${reserva.horario}?"
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onCancelarReserva(reserva)
                            reservaACancelar = null
                        }
                    ) {
                        Text(text = "Sí, cancelar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { reservaACancelar = null }
                    ) {
                        Text(text = "Volver")
                    }
                }
            )
        }
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva,
    onCancelarClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = reserva.clase.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "${reserva.horario} · ${reserva.clase.sala}"
            )

            Text(
                text = reserva.clase.duracion
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reserva.estado,
                    color = Color(0xFF00866A),
                    fontWeight = FontWeight.SemiBold
                )

                // Solo se puede cancelar una reserva que aún está confirmada.
                if (reserva.estado == "Confirmada") {
                    OutlinedButton(
                        onClick = onCancelarClick,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFFB3261E)
                        )
                    ) {
                        Text(text = "Cancelar")
                    }
                }
            }
        }
    }
}