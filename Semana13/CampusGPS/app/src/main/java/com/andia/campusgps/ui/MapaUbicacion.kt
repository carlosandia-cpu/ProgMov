package com.andia.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.util.Log
import com.andia.campusgps.R
import com.andia.campusgps.model.Lugar
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.ui.theme.AzulNoche
import com.andia.campusgps.ui.theme.AzulPrincipal
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercano: Lugar?,
    radioMetros: Float?,
    esModoOscuro: Boolean,
    onToggleModoOscuro: () -> Unit,
    onSeleccionar: (Int) -> Unit,
    onUbicacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    val mapProperties = remember(esModoOscuro, contexto) {
        val estilo = if (esModoOscuro) {
            try {
                MapStyleOptions.loadRawResourceStyle(contexto, R.raw.map_style_dark)
            } catch (e: Exception) {
                Log.e("CampusGPS", "No se pudo cargar el estilo oscuro del mapa", e)
                null
            }
        } else {
            null
        }
        MapProperties(mapStyleOptions = estilo)
    }

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        estado.posicion.latitud,
                        estado.posicion.longitud
                    ),
                    16f
                )
            )
        }
    }

    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull {
            it.lugar.id == seleccionado
        }?.lugar ?: return@LaunchedEffect

        camara.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lugar.latitud, lugar.longitud),
                17f
            )
        )
    }

    Box(modifier = modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camara,
            properties = mapProperties,
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = true,
                mapToolbarEnabled = false
            )
        ) {
            if (estado is UbicacionUi.Lista) {
                val centro = LatLng(
                    estado.posicion.latitud,
                    estado.posicion.longitud
                )

                Marker(
                    state = rememberUpdatedMarkerState(position = centro),
                    title = "Estás aquí",
                    icon = BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED
                    )
                )

                if (radioMetros != null) {
                    Circle(
                        center = centro,
                        radius = radioMetros.toDouble(),
                        strokeColor = AzulPrincipal,
                        strokeWidth = 4f,
                        fillColor = AzulPrincipal.copy(alpha = 0.15f)
                    )
                }
            }

            lugares.forEach { item ->
                val lugar = item.lugar

                key(lugar.id) {
                    val markerState = rememberUpdatedMarkerState(
                        position = LatLng(
                            lugar.latitud,
                            lugar.longitud
                        )
                    )

                    LaunchedEffect(seleccionado) {
                        if (seleccionado == lugar.id) {
                            markerState.showInfoWindow()
                        }
                    }

                    Marker(
                        state = markerState,
                        title = lugar.nombre,
                        snippet = lugar.categoria,
                        icon = BitmapDescriptorFactory.defaultMarker(
                            if (lugar.id == masCercano?.id) {
                                BitmapDescriptorFactory.HUE_GREEN
                            } else {
                                BitmapDescriptorFactory.HUE_AZURE
                            }
                        ),
                        onClick = {
                            onSeleccionar(lugar.id)
                            false
                        }
                    )
                }
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 16.dp, end = 16.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            IconButton(
                onClick = onToggleModoOscuro,
                modifier = Modifier.size(48.dp)
            ) {
                Text(
                    text = if (esModoOscuro) "☀️" else "🌙",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 18.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 8.dp
        ) {
            IconButton(
                onClick = onUbicacion,
                enabled = estado !is UbicacionUi.Buscando,
                modifier = Modifier.size(52.dp)
            ) {
                Text(
                    text = "◎",
                    style = MaterialTheme.typography.headlineMedium,
                    color = AzulNoche
                )
            }
        }
    }
}
