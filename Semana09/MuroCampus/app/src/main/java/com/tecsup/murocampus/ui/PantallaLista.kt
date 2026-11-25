package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit
) {

    val estado by vm.lista.collectAsStateWithLifecycle()
    val filtro by vm.usuarioFiltro.collectAsStateWithLifecycle()
    val busqueda by vm.busquedaQuery.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                FilterChip(
                    selected = filtro == null,
                    onClick = { vm.filtrar(null) },
                    label = { Text("Todos") }
                )
            }

            items((1..10).toList()) { usuario ->

                FilterChip(
                    selected = filtro == usuario,
                    onClick = { vm.filtrar(usuario) },
                    label = { Text("Usuario $usuario") }
                )
            }
        }

        OutlinedTextField(
            value = busqueda,
            onValueChange = { vm.buscar(it) },
            placeholder = { Text("Buscar por título...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar"
                )
            },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(onClick = { vm.buscar("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar búsqueda"
                        )
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            when (val actual = estado) {

                UiState.Cargando -> {
                    Cargando()
                }

                is UiState.Error -> {
                    ErrorConReintento(
                        mensaje = actual.mensaje,
                        onReintentar = { vm.cargar() }
                    )
                }

                is UiState.Exito -> {

                    if (actual.datos.isEmpty()) {

                        Text(
                            text = if (busqueda.isNotBlank()) "Sin coincidencias" else "Sin publicaciones",
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            items(
                                items = actual.datos,
                                key = { it.id }
                            ) { post ->

                                TarjetaPost(
                                    userId = post.userId,
                                    title = post.title,
                                    body = post.body,
                                    onClick = { onAbrir(post.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}