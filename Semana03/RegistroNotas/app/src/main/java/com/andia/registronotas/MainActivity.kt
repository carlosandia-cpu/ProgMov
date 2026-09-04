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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
                    }
                )

                CursoSlider(
                    nombre = "Programación Orientada a Objetos",
                    peso = "25%",
                    nota = notaPoo,
                    onNotaChange = {
                        notaPoo =
                            it.roundToInt().toFloat()
                    }
                )

                CursoSlider(
                    nombre = "Programación en Móviles",
                    peso = "30%",
                    nota = notaMoviles,
                    onNotaChange = {
                        notaMoviles =
                            it.roundToInt().toFloat()
                    }
                )

                CursoSlider(
                    nombre = "Base de Datos",
                    peso = "25%",
                    nota = notaBaseDatos,
                    onNotaChange = {
                        notaBaseDatos =
                            it.roundToInt().toFloat()
                    }
                )

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                Text(
                    text = "Desarrollado por: Carlos Andia",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier.align(
                        Alignment.CenterHorizontally
                    )
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