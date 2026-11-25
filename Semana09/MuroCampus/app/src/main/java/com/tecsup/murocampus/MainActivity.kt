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

                BackHandler(enabled = idSeleccionado != null) {
                    vm.cerrarDetalle()
                    idSeleccionado = null
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {

                        TopAppBar(
                            title = {
                                Text(
                                    if (idSeleccionado == null)
                                        "MuroCampus"
                                    else
                                        "Detalle"
                                )
                            },
                            navigationIcon = {
                                if (idSeleccionado != null) {
                                    TextButton(
                                        onClick = {
                                            vm.cerrarDetalle()
                                            idSeleccionado = null
                                        }
                                    ) {
                                        Text(
                                            "Atrás",
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
                    }
                ) { padding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {

                        val id = idSeleccionado

                        if (id == null) {

                            PantallaLista(
                                vm = vm,
                                onAbrir = { seleccionado ->
                                    vm.abrir(seleccionado)
                                    idSeleccionado = seleccionado
                                }
                            )

                        } else {

                            PantallaDetalle(
                                vm = vm,
                                id = id
                            )
                        }
                    }
                }
            }
        }
    }
}