package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {

    TarjetaAvisos("Historial") {

        if (avisos.isEmpty()) {

            Text("Sin avisos todavía")

        } else {

            val recientes = avisos.take(20)

            recientes.forEachIndexed { index, aviso ->

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = aviso.origen.texto,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = aviso.hora,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Text(
                        text = aviso.titulo,
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = aviso.texto,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (index < recientes.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}