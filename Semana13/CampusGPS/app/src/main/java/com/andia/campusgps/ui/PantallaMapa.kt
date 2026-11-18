
package com.andia.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.util.formatearDistancia
import com.andia.campusgps.viewmodel.MapaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val contexto = LocalContext.current

    // Permisos de ubicación
    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun tienePermiso(): Boolean {
        val permisoPreciso =
            ContextCompat.checkSelfPermission(
                contexto,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        val permisoAproximado =
            ContextCompat.checkSelfPermission(
                contexto,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        return permisoPreciso || permisoAproximado
    }

    fun alPulsar() {
        if (tienePermiso()) {
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

    val distanciaCercana = lugares.firstOrNull {
        it.lugar.id == masCercano?.id
    }?.metros

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CampusGPS",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,
                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Mapa superior
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            // Panel inferior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                // Botón principal
                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(
                        text = if (estado is UbicacionUi.Buscando) {
                            "Buscando..."
                        } else {
                            "Obtener mi ubicación"
                        }
                    )
                }

                // Botón secundario del Commit 8
                OutlinedButton(
                    onClick = {
                        masCercano?.let { lugar ->
                            vm.seleccionar(lugar.id)
                        }
                    },
                    enabled = masCercano != null,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Ir al más cercano")
                }

                // Información del más cercano
                if (masCercano != null) {
                    Text(
                        text = "Más cercano: ${masCercano!!.nombre} " +
                                "(${distanciaCercana?.let {
                                    formatearDistancia(it)
                                } ?: "Sin calcular"})",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(
                            horizontal = 16.dp
                        )
                    )
                }

                // Estado actual de ubicación
                when (val actual = estado) {

                    is UbicacionUi.Lista -> {
                        Text(
                            text = "Latitud: ${actual.posicion.latitud}  ·  " +
                                    "Longitud: ${actual.posicion.longitud}",
                            style = MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    is UbicacionUi.Error -> {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor =
                                    MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            )
                        ) {
                            Text(
                                text = actual.mensaje,
                                modifier = Modifier.padding(12.dp),
                                color =
                                    MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = "Pulsa el botón para empezar",
                            style = MaterialTheme.typography.bodySmall,
                            color =
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Lista inferior de cinco lugares
                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { id ->
                        vm.seleccionar(id)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
        }
    }
}
