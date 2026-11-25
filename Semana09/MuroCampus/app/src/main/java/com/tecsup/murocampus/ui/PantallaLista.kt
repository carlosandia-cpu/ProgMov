package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onAbrir: (Int) -> Unit
) {

    val estado by vm.lista.collectAsStateWithLifecycle()

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