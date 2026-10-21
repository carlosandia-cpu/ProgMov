package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.ui.components.soles

@Composable
fun GraficoBarras(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val maximo = datos.maxOfOrNull { it.valor } ?: 0.0

    if (datos.isEmpty() || maximo <= 0.0) {
        Text(
            "Aún no hay ingresos esta semana",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
    } else {
        Column(modifier) {
            Row(
                Modifier.fillMaxWidth().height(150.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                datos.forEach { d ->
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom
                    ) {
                        Text(d.valor.soles(), style = MaterialTheme.typography.labelSmall)
                        val alto = (110.0 * d.valor / maximo).coerceAtLeast(4.0)
                        Canvas(Modifier.fillMaxWidth(0.55f).height(alto.dp).padding(top = 4.dp)) {
                            drawRoundRect(d.color, cornerRadius = CornerRadius(8.dp.toPx()))
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().padding(top = 6.dp)) {
                datos.forEach { d ->
                    Text(
                        d.etiqueta,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}