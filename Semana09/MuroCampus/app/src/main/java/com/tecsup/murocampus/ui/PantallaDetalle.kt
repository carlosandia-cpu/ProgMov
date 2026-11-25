package com.tecsup.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    id: Int,
    onEliminado: () -> Unit = {}
) {

    val estado by vm.detalle.collectAsStateWithLifecycle()
    val estadoEliminacion by vm.eliminacion.collectAsStateWithLifecycle()

    var mostrarConfirmacion by remember { mutableStateOf(false) }

    val eliminando = estadoEliminacion == UiState.Cargando

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                if (!eliminando) {
                    mostrarConfirmacion = false
                }
            },
            title = { Text("Eliminar publicación") },
            text = {
                Column {
                    Text("¿Estás seguro de que deseas eliminar esta publicación?")
                    if (eliminando) {
                        Spacer(Modifier.height(12.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        vm.eliminar(id) {
                            mostrarConfirmacion = false
                            onEliminado()
                        }
                    },
                    enabled = !eliminando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarConfirmacion = false },
                    enabled = !eliminando
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

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

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Usuario ${datos.post.userId}",
                            color = Color(0xFF1565C0)
                        )

                        Button(
                            onClick = { mostrarConfirmacion = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            enabled = !eliminando
                        ) {
                            Text("Eliminar")
                        }
                    }

                    if (eliminando) {
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }

                    if (estadoEliminacion is UiState.Error) {
                        Spacer(Modifier.height(8.dp))
                        ErrorConReintento(
                            mensaje = (estadoEliminacion as UiState.Error).mensaje,
                            onReintentar = {
                                vm.eliminar(id) {
                                    onEliminado()
                                }
                            }
                        )
                    }

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