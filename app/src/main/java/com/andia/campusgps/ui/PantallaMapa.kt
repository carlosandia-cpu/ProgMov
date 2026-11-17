
package com.andia.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(
    vm: MapaViewModel
) {
    val estado by vm.estado.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "CampusGPS",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Explora los lugares de tu campus",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { vm.buscar() },
            enabled = estado !is UbicacionUi.Buscando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Obtener mi ubicación")
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val actual = estado) {

            is UbicacionUi.Inicial -> {
                Text(
                    text = "Presiona el botón para buscar tu ubicación.",
                    textAlign = TextAlign.Center
                )
            }

            is UbicacionUi.Buscando -> {
                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(12.dp))

                Text("Buscando ubicación...")
            }

            is UbicacionUi.Lista -> {
                Text(
                    text = "Ubicación obtenida correctamente",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Latitud: ${actual.posicion.latitud}"
                )

                Text(
                    text = "Longitud: ${actual.posicion.longitud}"
                )
            }

            is UbicacionUi.Error -> {
                Text(
                    text = actual.mensaje,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
