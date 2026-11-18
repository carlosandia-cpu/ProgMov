
package com.andia.campusgps.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.model.UbicacionUi
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    modifier: Modifier = Modifier
) {

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(-12.0464, -77.0428),
            12f
        )
    }

    // Mover la cámara a la ubicación del usuario
    LaunchedEffect(estado) {
        if (estado is UbicacionUi.Lista) {
            val posicion = LatLng(
                estado.posicion.latitud,
                estado.posicion.longitud
            )

            camara.animate(
                CameraUpdateFactory.newLatLngZoom(
                    posicion,
                    16f
                )
            )
        }
    }

    // Mover la cámara al lugar seleccionado
    LaunchedEffect(seleccionado) {
        val lugar = lugares.firstOrNull {
            it.lugar.id == seleccionado
        }?.lugar ?: return@LaunchedEffect

        camara.animate(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    lugar.latitud,
                    lugar.longitud
                ),
                17f
            )
        )
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = camara,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        )
    ) {

        // Marcador rojo del usuario
        if (estado is UbicacionUi.Lista) {
            Marker(
                state = rememberUpdatedMarkerState(
                    position = LatLng(
                        estado.posicion.latitud,
                        estado.posicion.longitud
                    )
                ),
                title = "Estás aquí"
            )
        }

        // Marcadores azules de los cinco lugares
        lugares.forEach { item ->

            val lugar = item.lugar

            key(lugar.id) {

                val markerState = rememberUpdatedMarkerState(
                    position = LatLng(
                        lugar.latitud,
                        lugar.longitud
                    )
                )

                // Mostrar el nombre al seleccionar
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
                        BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }
    }
}
