package com.example.tecsupfit.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tecsupfit.components.ClaseCard
import com.example.tecsupfit.data.clasesDisponibles
import com.example.tecsupfit.model.ClaseGimnasio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClaseClick: (ClaseGimnasio) -> Unit = {}
) {
    var filtroSeleccionado by remember {
        mutableStateOf("Hoy")
    }

    val filtros = listOf(
        "Hoy",
        "Esta semana"
    )

    val clasesFiltradas = if (filtroSeleccionado == "Hoy") {
        clasesDisponibles.filter { clase ->
            clase.categoria == "Hoy"
        }
    } else {
        clasesDisponibles
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "TECSUP Fit",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Hola, Diego"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyRow(
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtros) { filtro ->
                    FilterChip(
                        selected = filtroSeleccionado == filtro,
                        onClick = {
                            filtroSeleccionado = filtro
                        },
                        label = {
                            Text(text = filtro)
                        }
                    )
                }
            }

            Text(
                text = "Clases disponibles",
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 12.dp
                ),
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = clasesFiltradas,
                    key = { clase -> clase.id }
                ) { clase ->
                    ClaseCard(
                        clase = clase,
                        onClick = {
                            onClaseClick(clase)
                        }
                    )
                }
            }
        }
    }
}