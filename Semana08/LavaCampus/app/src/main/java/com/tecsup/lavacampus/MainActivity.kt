package com.tecsup.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.navigation.NavLava
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.LavaViewModelFactory
import com.tecsup.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as LavaApp

        setContent {
            val sesionVm: SesionViewModel = viewModel(
                factory = LavaViewModelFactory(app.repository, app.preferencias)
            )
            val modoOscuro by sesionVm.modoOscuro.collectAsState()
            val usuario by sesionVm.usuarioActual.collectAsState()
            val rol = usuario?.rol?.let { Rol.valueOf(it) } ?: Rol.CLIENTE

            LavaTheme(rol = rol, modoOscuro = modoOscuro) {
                NavLava(sesionVm, app.repository, app.preferencias)
            }
        }
    }
}