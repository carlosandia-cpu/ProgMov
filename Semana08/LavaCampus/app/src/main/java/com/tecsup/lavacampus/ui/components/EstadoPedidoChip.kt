package com.tecsup.lavacampus.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.EstadoPedido

@Composable
fun EstadoPedidoChip(estado: EstadoPedido, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(50),
        color = estado.color.copy(alpha = 0.15f),
        modifier = modifier
    ) {
        Text(
            text = estado.etiqueta,
            color = estado.color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}