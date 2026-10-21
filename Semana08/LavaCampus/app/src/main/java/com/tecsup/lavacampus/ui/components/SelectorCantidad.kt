package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.util.Locale

/** Control con botones menos y más. Avisa con onDelta(+paso) o onDelta(-paso). */
@Composable
fun SelectorCantidad(
    valor: Double,
    paso: Double,
    minimo: Double,
    maximo: Double,
    onDelta: (Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val texto = if (paso % 1.0 != 0.0) String.format(Locale.US, "%.1f", valor) else valor.toInt().toString()

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        FilledTonalIconButton(onClick = { onDelta(-paso) }, enabled = valor >= minimo) {
            Icon(Icons.Default.Remove, contentDescription = "Menos")
        }
        Text(
            text = texto,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 42.dp)
        )
        FilledTonalIconButton(onClick = { onDelta(paso) }, enabled = valor < maximo) {
            Icon(Icons.Default.Add, contentDescription = "Más")
        }
    }
}