package com.andia.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.andia.campusgps.ui.theme.*
import com.andia.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    var mostrarInicio by rememberSaveable { mutableStateOf(true) }

    if (mostrarInicio) {
        PantallaBienvenida(onComenzar = { mostrarInicio = false })
        return
    }

    val contexto = LocalContext.current

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun obtenerUbicacion() {
        val fino = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val aproximado = ContextCompat.checkSelfPermission(
            contexto, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (fino || aproximado) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun abrirAjustes() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", contexto.packageName, null)
        }
        contexto.startActivity(intent)
    }

    val cercano = lugares.firstOrNull { it.lugar.id == masCercano?.id }

    Scaffold(
        containerColor = FondoApp,
        topBar = { CabeceraCampus(cantidad = lugares.size) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                onSeleccionar = { id -> vm.seleccionar(id) },
                onUbicacion = { obtenerUbicacion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            PanelCampus(
                estado = estado,
                lugarCercano = cercano,
                onUbicacion = { obtenerUbicacion() },
                onMasCercano = { masCercano?.let { vm.seleccionar(it.id) } },
                onAbrirAjustes = { abrirAjustes() },
                onReintentar = { obtenerUbicacion() }
            )

            // Lista fija de 220 dp, como pide el laboratorio
            Surface(color = FondoApp) {
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { id -> vm.seleccionar(id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .navigationBarsPadding()
                )
            }
        }
    }
}