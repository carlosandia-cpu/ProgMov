package com.saludplus.citas.ui.screens.auth

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.saludplus.citas.data.repository.Repositorio
import com.saludplus.citas.ui.components.BotonPrimario
import com.saludplus.citas.ui.theme.TealOscuro
import com.saludplus.citas.ui.theme.TealPrimario
import androidx.compose.runtime.getValue

@Composable
fun SplashScreen(
    onCrearCuenta: () -> Unit,
    onIniciarSesion: () -> Unit
) {
    // Pulso suave del halo detrás del logo
    val transicion = rememberInfiniteTransition(label = "pulso")
    val escala by transicion.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "escala"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(TealPrimario, TealOscuro)))
    ) {
        // Círculos decorativos de fondo
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.White.copy(alpha = 0.07f),
                radius = size.width * 0.55f,
                center = Offset(size.width * 0.95f, size.height * 0.08f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = size.width * 0.45f,
                center = Offset(size.width * 0.02f, size.height * 0.62f)
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.06f),
                radius = size.width * 0.3f,
                center = Offset(size.width * 0.9f, size.height * 0.95f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 28.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.weight(1f))

            // Logo con halo animado
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(170.dp)) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .graphicsLayer {
                            scaleX = escala
                            scaleY = escala
                        }
                        .background(Color.White.copy(alpha = 0.16f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(112.dp)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalHospital,
                        contentDescription = "Logo de SaludPlus",
                        tint = TealPrimario,
                        modifier = Modifier.size(64.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "SaludPlus",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Agenda tus citas médicas de forma rápida y segura",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.88f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Datos rápidos
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DatoRapido(Icons.Default.LocationOn, "${Repositorio.sedes.size} sedes")
                DatoRapido(Icons.Default.MedicalServices, "${Repositorio.medicos.size} doctores")
                DatoRapido(Icons.Default.Schedule, "En minutos")
            }

            Spacer(Modifier.weight(1f))

            BotonPrimario(texto = "Crear cuenta", onClick = onCrearCuenta)
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onIniciarSesion,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.5.dp, Color.White),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Iniciar sesión", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
private fun DatoRapido(icono: ImageVector, texto: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color.White.copy(alpha = 0.16f))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}