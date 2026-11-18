
package com.andia.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andia.campusgps.data.LugaresRepository
import com.andia.campusgps.data.UbicacionRepository
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.model.Posicion
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    private val _seleccionado =
        MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    fun buscar() {
        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    _estado.value = UbicacionUi.Lista(posicion)
                    actualizarDistancias(posicion)
                }
                .onFailure { error ->
                    _estado.value = UbicacionUi.Error(
                        mensajeDe(error)
                    )
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás"
        )
    }

    // Paso 6 del PDF: seleccionar(id: Int?)
    // Paso 7 del PDF: masCercano

    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { lugar ->
                LugarConDistancia(
                    lugar,
                    distanciaMetros(pos, lugar)
                )
            }
            .sortedBy { it.metros }

        // Verificación de distancias en Logcat
        _lugares.value.forEach { item ->
            Log.d(
                "CampusGPS",
                "${item.lugar.nombre}: ${item.metros} metros"
            )
        }
    }

    private fun mensajeDe(e: Throwable): String =
        when (e) {
            is SecurityException ->
                "Falta el permiso de ubicación"

            else ->
                e.message ?: "No se pudo obtener la ubicación"
        }
}
