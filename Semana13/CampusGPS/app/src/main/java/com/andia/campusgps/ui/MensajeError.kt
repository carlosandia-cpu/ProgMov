
package com.andia.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andia.campusgps.model.TipoError
import com.andia.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = error.mensaje,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            when (error.tipo) {

                TipoError.SIN_PERMISO -> {
                    Button(
                        onClick = onAbrirAjustes
                    ) {
                        Text("Abrir ajustes")
                    }
                }

                TipoError.GPS_APAGADO -> {
                    OutlinedButton(
                        onClick = onReintentar
                    ) {
                        Text("Reintentar")
                    }
                }

                TipoError.SIN_PLAY_SERVICES -> {
                    // Solo se muestra el mensaje
                }

                TipoError.OTRO -> {
                    OutlinedButton(
                        onClick = onReintentar
                    ) {
                        Text("Reintentar")
                    }
                }
            }
        }
    }
}
