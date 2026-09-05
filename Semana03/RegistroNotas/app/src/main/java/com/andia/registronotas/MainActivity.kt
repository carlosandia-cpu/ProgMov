package com.andia.registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                RegistroNotasApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroNotasApp() {

    var notaFundamentos by remember {
        mutableFloatStateOf(0f)
    }

    var notaPoo by remember {
        mutableFloatStateOf(0f)
    }

    var notaMoviles by remember {
        mutableFloatStateOf(0f)
    }

    var notaBaseDatos by remember {
        mutableFloatStateOf(0f)
    }

    var redondearPromedio by remember {
        mutableStateOf(false)
    }

    var notasConfirmadas by remember {
        mutableStateOf(false)
    }

    var calculado by remember {
        mutableStateOf(false)
    }

    // PROMEDIO PONDERADO
    val promedioPonderado =
        notaFundamentos.toDouble() * 0.20 +
                notaPoo.toDouble() * 0.25 +
                notaMoviles.toDouble() * 0.30 +
                notaBaseDatos.toDouble() * 0.25

    // PROMEDIO FINAL
    val promedioFinal = if (redondearPromedio) {
        promedioPonderado.roundToInt().toDouble()
    } else {
        promedioPonderado
    }

    // OBSERVACIÓN
    val observacion = when {
        promedioFinal >= 17 -> "EXCELENTE"
        promedioFinal >= 13 -> "APROBADO"
        promedioFinal >= 10 -> "EN RECUPERACIÓN"
        else -> "DESAPROBADO"
    }

    // COLOR DE LA OBSERVACIÓN
    val colorObservacion = when (observacion) {
        "EXCELENTE" -> Color(0xFF1B5E20)
        "APROBADO" -> Color(0xFF388E3C)
        "EN RECUPERACIÓN" -> Color(0xFFFFA000)
        else -> Color(0xFFD32F2F)
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = "Registro de Notas",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6750A4),
                    titleContentColor = Color.White
                )
            )
        },

        bottomBar = {

            Surface(
                color = Color.White,
                shadowElevation = 4.dp
            ) {

                Text(
                    text = "Desarrollado por: Carlos Andia",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }

    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF4F0FF),
                            Color.White
                        )
                    )
                )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(20.dp)
            ) {

                Text(
                    text = "Notas del ciclo",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Desliza para asignar la nota de cada curso.",
                    color = Color.Gray,
                    modifier = Modifier.padding(
                        top = 4.dp,
                        bottom = 22.dp
                    )
                )

                CursoSlider(
                    nombre = "Fundamentos de Programación",
                    peso = "20%",
                    nota = notaFundamentos,
                    onNotaChange = {

                        notaFundamentos =
                            it.roundToInt().toFloat()

                        calculado = false
                    }
                )

                CursoSlider(
                    nombre = "Programación Orientada a Objetos",
                    peso = "25%",
                    nota = notaPoo,
                    onNotaChange = {

                        notaPoo =
                            it.roundToInt().toFloat()

                        calculado = false
                    }
                )

                CursoSlider(
                    nombre = "Programación en Móviles",
                    peso = "30%",
                    nota = notaMoviles,
                    onNotaChange = {

                        notaMoviles =
                            it.roundToInt().toFloat()

                        calculado = false
                    }
                )

                CursoSlider(
                    nombre = "Base de Datos",
                    peso = "25%",
                    nota = notaBaseDatos,
                    onNotaChange = {

                        notaBaseDatos =
                            it.roundToInt().toFloat()

                        calculado = false
                    }
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically,
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Redondear promedio final",
                        fontWeight = FontWeight.Medium
                    )

                    Switch(
                        checked = redondearPromedio,
                        onCheckedChange = {

                            redondearPromedio = it
                            calculado = false
                        }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = notasConfirmadas,
                        onCheckedChange = {

                            notasConfirmadas = it

                            if (!it) {
                                calculado = false
                            }
                        }
                    )

                    Text(
                        text =
                            "Confirmo que las notas son correctas"
                    )
                }

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                Button(
                    onClick = {
                        calculado = true
                    },
                    enabled = notasConfirmadas,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {

                    Text(
                        text = "CALCULAR PROMEDIO",
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                if (!calculado) {

                    Text(
                        text =
                            "Asigna las notas y confirma para calcular.",
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                } else {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 6.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Text(
                                text = "Resultado",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(14.dp)
                            )

                            Text(
                                text =
                                    "Promedio ponderado: %.2f"
                                        .format(
                                            promedioPonderado
                                        ),
                                fontSize = 17.sp
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(8.dp)
                            )

                            if (redondearPromedio) {

                                Text(
                                    text =
                                        "Promedio final: " +
                                                "${promedioFinal.toInt()} " +
                                                "(redondeado)",
                                    fontSize = 18.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                )

                            } else {

                                Text(
                                    text =
                                        "Promedio final: %.2f"
                                            .format(
                                                promedioFinal
                                            ),
                                    fontSize = 18.sp,
                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(16.dp)
                            )

                            Surface(
                                color = colorObservacion,
                                shape =
                                    RoundedCornerShape(50.dp)
                            ) {

                                Text(
                                    text = observacion,
                                    color = Color.White,
                                    fontWeight =
                                        FontWeight.Bold,
                                    modifier =
                                        Modifier.padding(
                                            horizontal = 18.dp,
                                            vertical = 8.dp
                                        )
                                )
                            }
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    Text(
                        text =
                            "✓ Promedio calculado correctamente",
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier.height(24.dp)
                )
            }
        }
    }
}

@Composable
fun CursoSlider(
    nombre: String,
    peso: String,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
                Arrangement.SpaceBetween,
            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(
                text = "$nombre ($peso)",
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Surface(
                color = Color(0xFF6750A4),
                shape = RoundedCornerShape(10.dp)
            ) {

                Text(
                    text = nota.toInt().toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    )
                )
            }
        }

        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f
        )
    }
}