package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ServiciosViewModel(private val repo: LavaRepository) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServicios()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun alternar(s: Servicio) {
        viewModelScope.launch { repo.cambiarActivo(s.id, !s.activo) }
    }

    /** Nuevo (id = 0) o editado. */
    fun guardar(servicio: Servicio, onListo: () -> Unit = {}) {
        if (servicio.nombre.isBlank() || servicio.precio <= 0.0) {
            _mensaje.value = "Completa el nombre y un precio mayor que 0"
            return
        }
        viewModelScope.launch {
            repo.guardarServicio(servicio)
            onListo()
        }
    }

    /** Un servicio que ya aparece en algún pedido no se elimina: solo se desactiva. */
    fun eliminar(servicio: Servicio) {
        viewModelScope.launch {
            if (repo.servicioTieneDetalles(servicio.id)) {
                _mensaje.value = "\"${servicio.nombre}\" ya está en pedidos y no se puede eliminar. Solo desactívalo."
            } else {
                repo.eliminarServicio(servicio)
                _mensaje.value = "Servicio eliminado"
            }
        }
    }
}