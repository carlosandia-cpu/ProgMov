package com.andia.campusgps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andia.campusgps.model.Lugar
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.ui.theme.*
import com.andia.campusgps.util.formatearDistancia

@Composable
fun LugarImagen(
    lugar: Lugar,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    val recurso = when (lugar.id) {
        1 -> "campus_biblioteca"
        2 -> "campus_cafeteria"
        3 -> "campus_laboratorio"
        4 -> "campus_auditorio"
        5 -> "campus_estacionamiento"
        else -> ""
    }

    val id = if (recurso.isNotEmpty()) {
        contexto.resources.getIdentifier(
            recurso,
            "drawable",
            contexto.packageName
        )
    } else {
        0
    }

    val simbolo = when (lugar.categoria) {
        "Estudio" -> "📚"
        "Comida" -> "☕"
        "Eventos" -> "🎭"
        "Servicios" -> "🅿"
        else -> "📍"
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(AzulClaro),
        contentAlignment = Alignment.Center
    ) {
        if (id != 0) {
            Image(
                painter = painterResource(id),
                contentDescription = lugar.nombre,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = simbolo,
                fontSize = 30.sp
            )
        }
    }
}

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    sinUbicacionParaRadio: Boolean = false,
    esModoOscuro: Boolean = false
) {
    val textoPrincipalModo = if (esModoOscuro) Color(0xFFF1F6FF) else TextoPrincipal
    val textoSecundarioModo = if (esModoOscuro) Color(0xFFB7C9DD) else TextoSecundario
    val fondoTarjeta = if (esModoOscuro) Color(0xFF1D3049) else Blanco
    val fondoSeleccionado = if (esModoOscuro) Color(0xFF2B4360) else AzulClaro
    val bordeModo = if (esModoOscuro) Color(0xFF47617F) else BordeSuave
    val azulAcento = if (esModoOscuro) Color(0xFF5CA8FF) else AzulPrincipal
    val verdeModo = if (esModoOscuro) Color(0xFF6DE8BF) else VerdeCampus

    if (lugares.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🔍",
                    fontSize = 36.sp
                )
                Text(
                    text = if (sinUbicacionParaRadio)
                        "Primero obtén tu ubicación"
                    else
                        "No hay lugares en esta selección",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoPrincipalModo,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (sinUbicacionParaRadio)
                        "Pulsa Mi ubicación para calcular las distancias y aplicar el radio."
                    else
                        "Prueba un radio mayor o cambia la categoría.",
                    fontSize = 13.sp,
                    color = textoSecundarioModo,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 10.dp
            )
        ) {
            items(
                items = lugares,
                key = { it.lugar.id }
            ) { item ->

                val activo = seleccionado == item.lugar.id

                Card(
                    onClick = {
                        onClick(item.lugar.id)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (activo) {
                            fondoSeleccionado
                        } else {
                            fondoTarjeta
                        }
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (activo) 2.dp else 1.dp,
                        if (activo) azulAcento else bordeModo
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (activo) 3.dp else 1.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LugarImagen(
                            lugar = item.lugar,
                            modifier = Modifier.size(70.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = item.lugar.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = textoPrincipalModo,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = item.lugar.categoria,
                                fontSize = 11.sp,
                                color = textoSecundarioModo
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (esModoOscuro) Color(0xFF204A46) else VerdeClaro
                        ) {
                            Text(
                                text = item.metros?.let {
                                    formatearDistancia(it)
                                } ?: "Sin calcular",
                                modifier = Modifier.padding(
                                    horizontal = 9.dp,
                                    vertical = 7.dp
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = verdeModo
                            )
                        }
                    }
                }
            }
        }
    }
}
