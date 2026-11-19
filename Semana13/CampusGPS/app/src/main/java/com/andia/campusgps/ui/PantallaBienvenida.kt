package com.andia.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.andia.campusgps.R
import com.andia.campusgps.ui.theme.*

@Composable
fun PantallaBienvenida(onComenzar: () -> Unit) {
    Box(Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.campus_inicio),
            contentDescription = "Ilustración de rutas del campus",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Degradado inferior para que el texto se lea bien
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.40f to Color.Transparent,
                        0.68f to AzulNoche.copy(alpha = 0.70f),
                        1f to AzulNoche.copy(alpha = 0.97f)
                    )
                )
        )

        // Título superior sobre una cápsula translúcida
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 20.dp, top = 24.dp, end = 20.dp)
                .background(
                    Blanco.copy(alpha = 0.82f),
                    RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "CAMPUS EXPLORER",
                color = AzulPrincipal,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 2.sp
            )
            Text(
                "CampusGPS",
                color = AzulNoche,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 36.sp
            )
            Text(
                "Tu campus, más cerca.",
                color = TextoSecundario,
                fontSize = 16.sp
            )
        }

        // Bloque inferior
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                "Descubre tu camino",
                color = Blanco,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Text(
                "Explora los lugares de tu campus y descubre " +
                        "cuál está más cerca de ti.",
                color = Blanco.copy(alpha = 0.92f),
                fontSize = 13.sp,
                lineHeight = 20.sp,
                textAlign = TextAlign.Center
            )

            // Tres datos de la app
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DatoBienvenida("📍", "Tu ubicación", Modifier.weight(1f))
                DatoBienvenida("🏫", "5 lugares", Modifier.weight(1f))
                DatoBienvenida("🧭", "El más cercano", Modifier.weight(1f))
            }

            Button(
                onClick = onComenzar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrincipal,
                    contentColor = Blanco
                ),
                border = BorderStroke(1.5.dp, Blanco.copy(alpha = 0.7f)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    "Comenzar a explorar  →",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DatoBienvenida(icono: String, texto: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Blanco.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
            .padding(vertical = 10.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(icono, fontSize = 18.sp)
        Text(
            texto,
            color = Blanco,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}