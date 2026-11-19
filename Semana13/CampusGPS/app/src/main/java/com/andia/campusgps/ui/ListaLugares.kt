
package com.andia.campusgps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.andia.campusgps.R
import com.andia.campusgps.model.Lugar
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.ui.theme.*
import com.andia.campusgps.util.formatearDistancia

@Composable
fun LugarImagen(
    lugar: Lugar,
    modifier: Modifier = Modifier
) {

    val imagen = when (lugar.id) {
        1 -> R.drawable.campus_biblioteca
        2 -> R.drawable.campus_cafeteria
        3 -> R.drawable.campus_laboratorio
        4 -> R.drawable.campus_auditorio
        5 -> R.drawable.campus_estacionamiento
        else -> R.drawable.campus_biblioteca
    }

    Image(
        painter = painterResource(imagen),
        contentDescription = lugar.nombre,
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(AzulClaro),
        contentScale = ContentScale.Crop
    )
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
        contentPadding = PaddingValues(
            horizontal = 16.dp,
            vertical = 10.dp
        ),
        verticalArrangement = Arrangement.spacedBy(11.dp)
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
                shape = RoundedCornerShape(19.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (activo) {
                        AzulClaro
                    } else {
                        Blanco
                    }
                ),
                border = BorderStroke(
                    width = if (activo) 2.dp else 1.dp,
                    color = if (activo) {
                        AzulPrincipal
                    } else {
                        BordeSuave
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
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
                        modifier = Modifier.size(78.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {

                        Text(
                            text = item.lugar.nombre,
                            color = TextoPrincipal,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = item.lugar.categoria,
                            color = TextoSecundario,
                            fontSize = 11.sp
                        )

                        if (activo) {
                            Text(
                                text = "Seleccionado",
                                color = AzulPrincipal,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Surface(
                        color = if (item.metros != null) {
                            VerdeClaro
                        } else {
                            AzulClaro
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = item.metros?.let {
                                formatearDistancia(it)
                            } ?: "—",
                            color = if (item.metros != null) {
                                VerdeCampus
                            } else {
                                TextoSecundario
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 8.dp
                            )
                        )
                    }
                }
            }
        }
    }
}
