package com.andia.campusgps.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.andia.campusgps.model.TipoError
import com.andia.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    esModoOscuro: Boolean = false
) {
    val fondoError = if (esModoOscuro) Color(0xFF4B2735) else MaterialTheme.colorScheme.errorContainer
    val textoError = if (esModoOscuro) Color(0xFFFFD4D4) else MaterialTheme.colorScheme.onErrorContainer
    val fondoBoton = if (esModoOscuro) Color(0xFFC93F56) else Color(0xFFB3261E)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = fondoError)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = error.mensaje,
                color = textoError,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            when (error.tipo) {
                TipoError.SIN_PERMISO -> {
                    Button(
                        onClick = onAbrirAjustes,
                        colors = if (esModoOscuro) ButtonDefaults.buttonColors(
                            containerColor = fondoBoton,
                            contentColor = Color.White
                        ) else ButtonDefaults.buttonColors()
                    ) { Text("Abrir ajustes") }
                }
                TipoError.GPS_APAGADO,
                TipoError.OTRO -> {
                    OutlinedButton(
                        onClick = onReintentar,
                        colors = if (esModoOscuro) {
                            ButtonDefaults.outlinedButtonColors(contentColor = textoError)
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        },
                        border = if (esModoOscuro) {
                            androidx.compose.foundation.BorderStroke(1.dp, textoError)
                        } else {
                            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        }
                    ) { Text("Reintentar") }
                }
                TipoError.SIN_PLAY_SERVICES -> Unit
            }
        }
    }
}
