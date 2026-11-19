package com.andia.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.ui.theme.*
import com.andia.campusgps.util.formatearDistancia
import java.util.Locale

@Composable
fun CabeceraCampus(cantidad: Int) {
    Surface(color = AzulNoche, shadowElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 18.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = AzulPrincipal,
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(42.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "CG",
                        color = Blanco,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Column(Modifier.weight(1f)) {
                Text(
                    "CampusGPS",
                    color = Blanco,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Tu campus, más cerca",
                    color = AzulSuave,
                    fontSize = 11.sp
                )
            }

            Surface(
                shape = RoundedCornerShape(50),
                color = AzulPrincipal.copy(alpha = 0.28f)
            ) {
                Text(
                    "$cantidad lugares",
                    color = Blanco,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(
                        horizontal = 11.dp,
                        vertical = 8.dp
                    )
                )
            }
        }
    }
}

@Composable
fun PanelCampus(
    estado: UbicacionUi,
    lugarCercano: LugarConDistancia?,
    onUbicacion: () -> Unit,
    onMasCercano: () -> Unit,
    onAbrirAjustes: () -> Unit,
    onReintentar: () -> Unit
) {
    Surface(
        color = Blanco,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Button(
                onClick = onUbicacion,
                enabled = estado !is UbicacionUi.Buscando,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    if (estado is UbicacionUi.Buscando)
                        "Buscando..."
                    else
                        "Obtener mi ubicación",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            OutlinedButton(
                onClick = onMasCercano,
                enabled = lugarCercano != null,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "Ir al más cercano",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            if (lugarCercano != null) {
                val distancia = lugarCercano.metros?.let { formatearDistancia(it) } ?: "—"
                Text(
                    "Más cercano: ${lugarCercano.lugar.nombre} ($distancia)",
                    color = VerdeCampus,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            when (estado) {
                is UbicacionUi.Lista -> {
                    Text(
                        String.format(
                            Locale.US,
                            "Latitud: %.4f  ·  Longitud: %.4f",
                            estado.posicion.latitud,
                            estado.posicion.longitud
                        ),
                        color = TextoSecundario,
                        fontSize = 11.sp
                    )
                }

                is UbicacionUi.Buscando -> {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                is UbicacionUi.Error -> {
                    MensajeError(
                        error = estado,
                        onAbrirAjustes = onAbrirAjustes,
                        onReintentar = onReintentar
                    )
                }

                else -> {
                    Text(
                        "Pulsa el botón para empezar",
                        color = TextoSecundario,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}