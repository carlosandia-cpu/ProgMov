
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    } else 0

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
    modifier: Modifier = Modifier
) {
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
                onClick = { onClick(item.lugar.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (activo) {
                        AzulClaro
                    } else {
                        Blanco
                    }
                ),
                border = androidx.compose.foundation.BorderStroke(
                    if (activo) 2.dp else 1.dp,
                    if (activo) AzulPrincipal else BordeSuave
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
                            color = TextoPrincipal,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = item.lugar.categoria,
                            fontSize = 11.sp,
                            color = TextoSecundario
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = VerdeClaro
                    ) {
                        Text(
                            text = item.metros?.let {
                                formatearDistancia(it)
                            } ?: "—",
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 7.dp
                            ),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = VerdeCampus
                        )
                    }
                }
            }
        }
    }
}
