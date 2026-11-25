package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int
) {

    val estado by vm.detalle.collectAsStateWithLifecycle()

    when (val actual = estado) {

        UiState.Cargando -> {
            Cargando("Cargando publicación…")
        }

        is UiState.Error -> {
            ErrorConReintento(
                mensaje = actual.mensaje,
                onReintentar = { vm.abrir(id) }
            )
        }

        is UiState.Exito -> {

            val datos = actual.datos

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {

                    Text(
                        text = "Usuario ${datos.post.userId}",
                        color = Color(0xFF1565C0)
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = datos.post.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(12.dp))

                    Text(datos.post.body)

                    Spacer(Modifier.height(16.dp))

                    HorizontalDivider()

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Comentarios (${datos.comentarios.size})",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                items(
                    items = datos.comentarios,
                    key = { it.id }
                ) { comentario ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = comentario.email,
                                color = Color(0xFF1565C0),
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(comentario.body)
                        }
                    }
                }
            }
        }
    }
}