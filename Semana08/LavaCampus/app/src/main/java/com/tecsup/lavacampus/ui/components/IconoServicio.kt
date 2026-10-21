package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Dibuja la lavadora (wash), la plancha (iron) o la camisa (shirt) según el campo icono. */
@Composable
fun IconoServicio(
    icono: String,
    modifier: Modifier = Modifier,
    tamano: Dp = 40.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    Canvas(modifier = modifier.size(tamano)) {
        val w = size.width
        val h = size.height
        val grosor = w * 0.07f
        val trazo = Stroke(width = grosor, cap = StrokeCap.Round, join = StrokeJoin.Round)

        when (icono) {
            "iron" -> {
                val plancha = Path().apply {
                    moveTo(w * 0.10f, h * 0.74f)
                    lineTo(w * 0.90f, h * 0.74f)
                    cubicTo(w * 0.90f, h * 0.50f, w * 0.78f, h * 0.30f, w * 0.60f, h * 0.30f)
                    lineTo(w * 0.30f, h * 0.30f)
                    cubicTo(w * 0.16f, h * 0.30f, w * 0.10f, h * 0.50f, w * 0.10f, h * 0.74f)
                    close()
                }
                drawPath(plancha, color, style = trazo)
                drawLine(color, Offset(w * 0.30f, h * 0.52f), Offset(w * 0.62f, h * 0.52f), strokeWidth = grosor, cap = StrokeCap.Round)
                drawLine(color, Offset(w * 0.22f, h * 0.86f), Offset(w * 0.78f, h * 0.86f), strokeWidth = grosor, cap = StrokeCap.Round)
            }
            "shirt" -> {
                val camisa = Path().apply {
                    moveTo(w * 0.35f, h * 0.15f)
                    lineTo(w * 0.10f, h * 0.30f)
                    lineTo(w * 0.20f, h * 0.48f)
                    lineTo(w * 0.30f, h * 0.42f)
                    lineTo(w * 0.30f, h * 0.85f)
                    lineTo(w * 0.70f, h * 0.85f)
                    lineTo(w * 0.70f, h * 0.42f)
                    lineTo(w * 0.80f, h * 0.48f)
                    lineTo(w * 0.90f, h * 0.30f)
                    lineTo(w * 0.65f, h * 0.15f)
                    quadraticTo(w * 0.50f, h * 0.32f, w * 0.35f, h * 0.15f)
                    close()
                }
                drawPath(camisa, color, style = trazo)
            }
            else -> { // wash: lavadora
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.15f, h * 0.08f),
                    size = Size(w * 0.70f, h * 0.84f),
                    cornerRadius = CornerRadius(w * 0.10f),
                    style = trazo
                )
                drawCircle(color = color, radius = w * 0.22f, center = Offset(w * 0.50f, h * 0.60f), style = trazo)
                drawCircle(color = color, radius = w * 0.04f, center = Offset(w * 0.30f, h * 0.21f))
                drawCircle(color = color, radius = w * 0.04f, center = Offset(w * 0.42f, h * 0.21f))
            }
        }
    }
}