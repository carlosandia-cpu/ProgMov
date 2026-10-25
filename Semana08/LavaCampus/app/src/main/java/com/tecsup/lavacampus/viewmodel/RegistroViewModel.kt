package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Rol
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegistroViewModel(private val repo: LavaRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    fun limpiarError() {
        _error.value = null
    }

    /** Valida los campos, comprueba que no existan y crea un estudiante. No inicia sesión. */
    fun registrar(
        nombre: String,
        codigo: String,
        correo: String,
        clave: String,
        repetir: String,
        onListo: () -> Unit
    ) {
        if (_guardando.value) return

        val n = nombre.trim()
        val cod = codigo.trim().uppercase()
        val mail = correo.trim().lowercase()

        val mensaje = when {
            n.isEmpty() || cod.isEmpty() || mail.isEmpty() || clave.isEmpty() || repetir.isEmpty() ->
                "Completa todos los campos"
            n.length < 3 -> "Escribe tu nombre completo"
            !CODIGO.matches(cod) -> "El código debe ser la letra U y 9 dígitos (ej. U202310234)"
            !CORREO.matches(mail) -> "Ingresa un correo válido"
            clave.length < 4 -> "La contraseña debe tener al menos 4 caracteres"
            clave != repetir -> "Las contraseñas no coinciden"
            else -> null
        }
        if (mensaje != null) {
            _error.value = mensaje
            return
        }

        _guardando.value = true
        viewModelScope.launch {
            val duplicado = when {
                repo.correoExiste(mail) -> "Ese correo ya está registrado"
                repo.codigoExiste(cod) -> "Ese código de estudiante ya está registrado"
                else -> null
            }
            if (duplicado != null) {
                _error.value = duplicado
                _guardando.value = false
                return@launch
            }

            repo.registrarUsuario(
                Usuario(
                    nombre = n,
                    codigo = cod,
                    correo = mail,
                    clave = clave,
                    rol = Rol.CLIENTE.name   // el registro siempre crea estudiantes
                )
            )
            _error.value = null
            _guardando.value = false
            onListo()
        }
    }

    private companion object {
        val CODIGO = Regex("^U\\d{9}$")
        val CORREO = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    }
}