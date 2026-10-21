package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SesionViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val modoOscuro: StateFlow<Boolean> = prefs.modoOscuro
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val notificaciones: StateFlow<Boolean> = prefs.notificaciones
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    /** Reglas del login. Si todo está bien, guarda el perfil y avisa con onExito. */
    fun login(correo: String, clave: String, rolSeleccionado: Rol, onExito: (Rol) -> Unit) {
        if (correo.isBlank() || clave.isBlank()) {
            _error.value = "Completa el correo y la contraseña"
            return
        }
        viewModelScope.launch {
            val usuario = repo.login(correo.trim(), clave)
            when {
                usuario == null -> _error.value = "Correo o contraseña incorrectos"
                usuario.rol != rolSeleccionado.name ->
                    _error.value = "Este usuario no pertenece al perfil ${
                        if (rolSeleccionado == Rol.ADMIN) "Administrador" else "Estudiante"
                    }"
                else -> {
                    _error.value = null
                    _usuarioActual.value = usuario
                    prefs.guardarSesion(rolSeleccionado, usuario.id)
                    onExito(rolSeleccionado)
                }
            }
        }
    }

    /** Lo usa el Splash: si hay perfil guardado, recupera el usuario y devuelve su rol. */
    suspend fun restaurarSesion(): Rol? {
        val perfil = prefs.perfilActivo.first()
        val id = prefs.usuarioId.first()
        if (perfil.isEmpty() || id == 0) return null
        val usuario = repo.obtenerUsuario(id) ?: return null
        _usuarioActual.value = usuario
        return Rol.valueOf(perfil)
    }

    /** Cambia al otro perfil sin pedir clave (modo práctica en el mismo dispositivo). */
    fun cambiarPerfil(destino: Rol, onListo: (Rol) -> Unit) {
        viewModelScope.launch {
            val usuario = repo.primerUsuarioDeRol(destino.name)
            if (usuario == null) {
                _error.value = "No existe un usuario con ese perfil"
                return@launch
            }
            _usuarioActual.value = usuario
            prefs.guardarSesion(destino, usuario.id)
            onListo(destino)
        }
    }

    fun cambiarModoOscuro(valor: Boolean) {
        viewModelScope.launch { prefs.guardarModoOscuro(valor) }
    }

    fun cambiarNotificaciones(valor: Boolean) {
        viewModelScope.launch { prefs.guardarNotificaciones(valor) }
    }

    fun cambiarRecibirPedidos(valor: Boolean) {
        viewModelScope.launch { prefs.guardarRecibirPedidos(valor) }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            prefs.cerrarSesion()
            _usuarioActual.value = null
        }
    }
}