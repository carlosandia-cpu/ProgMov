package com.tecsup.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import com.tecsup.avisoscampus.data.AvisosRepository
import com.tecsup.avisoscampus.model.Aviso
import com.tecsup.avisoscampus.model.PermisoUi
import kotlinx.coroutines.flow.*

class AvisosViewModel(
    private val repo: AvisosRepository
) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()
    val eventos: SharedFlow<Aviso> = _eventos.asSharedFlow()

    private val _permiso = MutableStateFlow<PermisoUi>(
        PermisoUi.SinPedir
    )
    val permiso: StateFlow<PermisoUi> = _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // TODO Paso 2: permisoRespondido

    // TODO Paso 3: avisarAhora

    // TODO Paso 5: recordatorio

    // TODO Paso 6: modo avión

    // TODO Paso 8: noSePudoMostrar
}