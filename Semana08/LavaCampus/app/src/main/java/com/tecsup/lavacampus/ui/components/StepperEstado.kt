package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecsup.lavacampus.model.EstadoPedido

private val ETAPAS = listOf(
    EstadoPedido.RECIBIDO,
    EstadoPedido.EN_PROCESO,
    EstadoPedido.LISTO,
    EstadoPedido.ENTREGADO
)

@Composable
fun StepperEstado(
    estado: EstadoPedido,
    modifier: Modifier = Modifier,
    vertical: Boolean = false,
    colorActivo: Color = MaterialTheme.colorScheme.primary,
    colorInactivo: Color = MaterialTheme.colorScheme.outlineVariant,
    colorMarca: Color = MaterialTheme.colorScheme.surface,
    colorTexto: Color = MaterialTheme.colorScheme.onSurface
) {
    if (estado == EstadoPedido.CANCELADO) {
        Text(
            "Pedido cancelado",
            color = EstadoPedido.CANCELADO.color,
            fontWeight = FontWeight.SemiBold,
            modifier = modifier
        )
    } else {
        val actual = ETAPAS.indexOf(estado)
        if (vertical) {
            StepperVertical(actual, modifier, colorActivo, colorInactivo, colorMarca, colorTexto)
        } else {
            StepperHorizontal(actual, modifier, colorActivo, colorInactivo, colorMarca, colorTexto)
        }
    }
}

@Composable
private fun StepperHorizontal(
    actual: Int,
    modifier: Modifier,
    colorActivo: Color,
    colorInactivo: Color,
    colorMarca: Color,
    colorTexto: Color
) {
    Column(modifier.fillMaxWidth()) {
        Canvas(Modifier.fillMaxWidth().height(24.dp)) {
            val paso = size.width / ETAPAS.size
            val cy = size.height / 2
            val r = 9.dp.toPx()
            ETAPAS.forEachIndexed { i, _ ->
                val cx = paso * (i + 0.5f)
                if (i < ETAPAS.size - 1) {
                    drawLine(
                        color = if (i < actual) colorActivo else colorInactivo,
                        start = Offset(cx + r, cy),
                        end = Offset(paso * (i + 1.5f) - r, cy),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                drawCircle(
                    color = if (i <= actual) colorActivo else colorInactivo,
                    radius = r,
                    center = Offset(cx, cy)
                )
                if (i == actual) {
                    drawCircle(color = colorMarca, radius = r * 0.4f, center = Offset(cx, cy))
                }
            }
        }
        Row(Modifier.fillMaxWidth().padding(top = 4.dp)) {
            ETAPAS.forEachIndexed { i, e ->
                Text(
                    text = e.etiqueta,
                    color = colorTexto,
                    fontSize = 11.sp,
                    fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun StepperVertical(
    actual: Int,
    modifier: Modifier,
    colorActivo: Color,
    colorInactivo: Color,
    colorMarca: Color,
    colorTexto: Color
) {
    Column(modifier) {
        ETAPAS.forEachIndexed { i, e ->
            Row(Modifier.height(IntrinsicSize.Min)) {
                Canvas(Modifier.width(24.dp).fillMaxHeight()) {
                    val r = 9.dp.toPx()
                    val cx = size.width / 2
                    if (i < ETAPAS.size - 1) {
                        drawLine(
                            color = if (i < actual) colorActivo else colorInactivo,
                            start = Offset(cx, r * 2),
                            end = Offset(cx, size.height),
                            strokeWidth = 3.dp.toPx(),
                            cap = StrokeCap.Round
                        )
                    }
                    drawCircle(
                        color = if (i <= actual) colorActivo else colorInactivo,
                        radius = r,
                        center = Offset(cx, r)
                    )
                    if (i == actual) {
                        drawCircle(color = colorMarca, radius = r * 0.4f, center = Offset(cx, r))
                    }
                }
                Column(
                    Modifier.padding(
                        start = 12.dp,
                        bottom = if (i < ETAPAS.size - 1) 22.dp else 0.dp
                    )
                ) {
                    Text(
                        text = e.etiqueta,
                        color = colorTexto,
                        fontWeight = if (i == actual) FontWeight.Bold else FontWeight.Normal
                    )
                    if (i == actual) {
                        Text("Etapa actual", color = colorTexto, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}