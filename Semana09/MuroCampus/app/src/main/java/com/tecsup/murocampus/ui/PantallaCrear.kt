package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(vm: PostsViewModel) {

    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var contenido by rememberSaveable {
        mutableStateOf("")
    }

    val estado by vm.creacion.collectAsStateWithLifecycle()

    val cargando = estado == UiState.Cargando

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !cargando
        )

        OutlinedTextField(
            value = contenido,
            onValueChange = { contenido = it },
            label = { Text("Contenido") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 5,
            enabled = !cargando
        )

        Button(
            onClick = {
                vm.publicar(titulo, contenido)
            },
            enabled = titulo.isNotBlank() &&
                    contenido.isNotBlank() &&
                    !cargando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publicar")
        }

        when (val actual = estado) {

            null -> Unit

            UiState.Cargando -> {

                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Publicando…")
            }

            is UiState.Exito -> {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFD9F2DC)
                    )
                ) {
                    Text(
                        text = "Publicado con id ${actual.datos.id}",
                        color = Color(0xFF1B5E20),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is UiState.Error -> {

                ErrorConReintento(
                    mensaje = actual.mensaje,
                    onReintentar = {
                        vm.publicar(titulo, contenido)
                    }
                )
            }
        }
    }
}