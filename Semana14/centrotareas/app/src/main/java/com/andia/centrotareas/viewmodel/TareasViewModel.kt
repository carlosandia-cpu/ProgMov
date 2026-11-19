
package com.andia.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.andia.centrotareas.data.TareasRepository
import com.andia.centrotareas.model.CargaUi
import com.andia.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(
    private val repo: TareasRepository
) : ViewModel() {

    // Tarea con progreso (Pasos 1 al 4)
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    // TODO Paso 1: tareaLargaMala()
    // TODO Paso 2: tareaLarga()
    // TODO Paso 3: progreso
    // TODO Paso 4: Job, corriendo y cancelar()

    // Contar números primos (Paso 5)
    private val _primos =
        MutableStateFlow<PrimosUi>(PrimosUi.Inicial)

    val primos: StateFlow<PrimosUi> =
        _primos.asStateFlow()

    // TODO Paso 5: contar() y cancelarPrimos()

    // Dos cargas (Pasos 6 y 7)
    private val _carga =
        MutableStateFlow<CargaUi>(CargaUi.Inicial)

    val carga: StateFlow<CargaUi> =
        _carga.asStateFlow()

    // TODO Paso 6: cargarUnaPorUna() y cargarALaVez()
    // TODO Paso 7: simularError y manejo de errores
}
