package com.tecsup.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun GraficoDona(datos: List<DatoGrafico>, modifier: Modifier = Modifier) {
    val total = datos.sumOf { it.valor }
    val fondo = MaterialTheme.colorScheme.outlineVariant

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(130.dp), contentAlignment = Alignment.Center) {
            Canvas(Modifier.fillMaxSize()) {
                val grosor = 22.dp.toPx()
                val diametro = size.minDimension - grosor
                val origen = Offset(grosor / 2, grosor / 2)
                val tamano = Size(diametro, diametro)

                if (total <= 0.0) {
                    drawArc(fondo, 0f, 360f, false, origen, tamano, style = Stroke(grosor))
                } else {
                    var inicio = -90f
                    datos.forEach { d ->
                        val barrido = (d.valor / total * 360.0).toFloat()
                        if (barrido > 0f) {
                            drawArc(d.color, inicio, barrido, false, origen, tamano, style = Stroke(grosor))
                        }
                        inicio += barrido
                    }
                }
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(total.toInt().toString(), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                Text("pedidos", style = MaterialTheme.typography.labelSmall)
            }
        }
        Spacer(Modifier.width(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            datos.forEach { d ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(10.dp).background(d.color, CircleShape))
                    Spacer(Modifier.width(8.dp))
                    Text("${d.etiqueta}: ${d.valor.toInt()}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}