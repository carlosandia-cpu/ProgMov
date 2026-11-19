
package com.andia.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andia.campusgps.data.LugaresRepository
import com.andia.campusgps.data.UbicacionRepository
import com.andia.campusgps.model.Lugar
import com.andia.campusgps.model.LugarConDistancia
import com.andia.campusgps.model.Posicion
import com.andia.campusgps.model.TipoError
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.andia.campusgps.ui.theme.CampusTheme

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

    val masCercano: StateFlow<Lugar?> =
        lugares.map { lista ->
            lista.firstOrNull {
                it.metros != null
            }?.lugar
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun buscar() {

        _estado.value = UbicacionUi.Buscando

        if (!ubicacionRepo.playServicesDisponible()) {
            mostrarError(
                "Este dispositivo no tiene Google Play Services",
                TipoError.SIN_PLAY_SERVICES
            )
            return
        }

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    _estado.value = UbicacionUi.Lista(posicion)
                    actualizarDistancias(posicion)
                }
                .onFailure { error ->
                    manejarError(error)
                }
        }
    }

    fun permisoDenegado() {
        mostrarError(
            "Sin permiso de ubicación no puedo mostrar dónde estás",
            TipoError.SIN_PERMISO
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }

    private fun actualizarDistancias(pos: Posicion) {
        _lugares.value = lugaresRepo.obtenerLugares()
            .map { lugar ->
                LugarConDistancia(
                    lugar = lugar,
                    metros = distanciaMetros(pos, lugar)
                )
            }
            .sortedBy { it.metros }
    }

    private fun manejarError(error: Throwable) {
        when (error) {

            is SecurityException -> {
                mostrarError(
                    "Sin permiso de ubicación no puedo mostrar dónde estás",
                    TipoError.SIN_PERMISO
                )
            }

            is IllegalStateException -> {
                mostrarError(
                    "Activa el GPS e intenta de nuevo",
                    TipoError.GPS_APAGADO
                )
            }

            else -> {
                mostrarError(
                    error.message ?: "No se pudo obtener la ubicación",
                    TipoError.OTRO
                )
            }
        }
    }

    private fun mostrarError(
        mensaje: String,
        tipo: TipoError
    ) {
        _estado.value = UbicacionUi.Error(
            mensaje = mensaje,
            tipo = tipo
        )
    }
}
