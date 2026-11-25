package com.tecsup.murocampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tecsup.murocampus.data.PostRepositoryRemote
import com.tecsup.murocampus.data.remote.RetrofitClient
import com.tecsup.murocampus.ui.PantallaLista
import com.tecsup.murocampus.ui.PantallaDetalle
import com.tecsup.murocampus.ui.PantallaCrear
import com.tecsup.murocampus.ui.theme.MuroCampusTheme
import com.tecsup.murocampus.viewmodel.PostsViewModel
import com.tecsup.murocampus.viewmodel.PostsViewModelFactory

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repo = PostRepositoryRemote(RetrofitClient.api)

        val factory = PostsViewModelFactory(repo)

        val vm = ViewModelProvider(
            this,
            factory
        )[PostsViewModel::class.java]

        setContent {
            MuroCampusTheme {

                var idSeleccionado by rememberSaveable {
                    mutableStateOf<Int?>(null)
                }

                var creando by rememberSaveable {
                    mutableStateOf(false)
                }

                val volver: () -> Unit = {

                    if (creando) {
                        vm.limpiarCreacion()
                        creando = false
                    } else {
                        vm.cerrarDetalle()
                        idSeleccionado = null
                    }
                }

                BackHandler(
                    enabled = idSeleccionado != null || creando
                ) {
                    volver()
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    topBar = {

                        TopAppBar(
                            title = {
                                Text(
                                    when {
                                        creando -> "Crear publicación"
                                        idSeleccionado != null -> "Detalle"
                                        else -> "MuroCampus"
                                    }
                                )
                            },

                            navigationIcon = {

                                if (idSeleccionado != null || creando) {

                                    TextButton(onClick = volver) {
                                        Text(
                                            text = "Atrás",
                                            color = Color.White
                                        )
                                    }
                                }
                            },

                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF1565C0),
                                titleContentColor = Color.White
                            )
                        )
                    },

                    floatingActionButton = {

                        if (idSeleccionado == null && !creando) {

                            FloatingActionButton(
                                onClick = {
                                    vm.limpiarCreacion()
                                    creando = true
                                },
                                containerColor = Color(0xFF1565C0),
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = "+",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                        }
                    }

                ) { padding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {

                        val id = idSeleccionado

                        when {

                            creando -> {
                                PantallaCrear(vm)
                            }

                            id != null -> {
                                PantallaDetalle(
                                    vm = vm,
                                    id = id
                                )
                            }

                            else -> {
                                PantallaLista(
                                    vm = vm,
                                    onAbrir = { seleccionado ->
                                        vm.abrir(seleccionado)
                                        idSeleccionado = seleccionado
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}