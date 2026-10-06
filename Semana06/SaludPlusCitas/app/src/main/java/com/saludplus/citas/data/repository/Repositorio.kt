package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Usuario

/**
 * Único repositorio de la app (object = una sola instancia compartida).
 * Todo vive en memoria: no hay base de datos y se pierde al cerrar la app.
 */
object Repositorio {

    // ---------- Colecciones ----------

    val usuarios = mutableStateListOf(
        Usuario("Paciente Demo", "paciente@saludplus.pe", "123456", "999888777")
    )

    val especialidades = listOf(
        Especialidad(1, "Cardiología", "Corazón y sistema circulatorio", "❤️"),
        Especialidad(2, "Pediatría", "Salud de niños y adolescentes", "🧒"),
        Especialidad(3, "Dermatología", "Piel, cabello y uñas", "🧴"),
        Especialidad(4, "Neurología", "Cerebro y sistema nervioso", "🧠"),
        Especialidad(5, "Traumatología", "Huesos, músculos y articulaciones", "🦴"),
        Especialidad(6, "Oftalmología", "Salud visual", "👁️"),
        Especialidad(7, "Ginecología", "Salud de la mujer", "🌸"),
        Especialidad(8, "Medicina General", "Atención integral y chequeos", "🩺")
    )

    val medicos = listOf(
        Medico(1, "Dra. Ana Torres", 1, 4.9, 12, "Consultorio 301"),
        Medico(2, "Dr. Carlos Mendoza", 1, 4.6, 8, "Consultorio 302"),
        Medico(3, "Dr. Luis Vega", 2, 4.7, 10, "Consultorio 201"),
        Medico(4, "Dra. Marta Quispe", 2, 4.8, 15, "Consultorio 202"),
        Medico(5, "Dra. Rosa Díaz", 3, 4.8, 9, "Consultorio 105"),
        Medico(6, "Dr. Jorge Paredes", 3, 4.5, 6, "Consultorio 106"),
        Medico(7, "Dr. Pablo Ramos", 4, 4.9, 18, "Consultorio 401"),
        Medico(8, "Dra. Elena Castro", 5, 4.6, 11, "Consultorio 110"),
        Medico(9, "Dr. Andrés Salas", 5, 4.4, 5, "Consultorio 111"),
        Medico(10, "Dra. Lucía Fernández", 6, 4.7, 13, "Consultorio 205"),
        Medico(11, "Dra. Sofía Núñez", 7, 4.8, 14, "Consultorio 305"),
        Medico(12, "Dr. Miguel Rojas", 8, 4.5, 7, "Consultorio 101")
    )

    // Horarios de atención de todos los médicos
    val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00",
        "14:00", "15:00", "16:00", "17:00"
    )

    // Días fijos de la Fase 1 (la Fase 2 los reemplaza por un calendario dinámico)
    val diasFijos = listOf(
        "2026-10-06", "2026-10-07", "2026-10-08", "2026-10-09", "2026-10-12"
    )

    // Citas de ejemplo: dos horarios ya ocupados para la Dra. Torres el 8/10
    // y una cita completada del usuario demo.
    val citas = mutableStateListOf(
        Cita(1, "otro@correo.com", 1, "2026-10-08", "09:00"),
        Cita(2, "otro@correo.com", 1, "2026-10-08", "10:00"),
        Cita(3, "paciente@saludplus.pe", 3, "2026-09-20", "15:00", "Completada")
    )

    private var siguienteCitaId = 4

    // ---------- Sesión ----------

    var usuarioActual: Usuario? by mutableStateOf(null)
        private set

    /** Devuelve false si ya existe un usuario con ese correo. */
    fun registrarUsuario(usuario: Usuario): Boolean {
        val existe = usuarios.any { it.correo.equals(usuario.correo, ignoreCase = true) }
        if (existe) return false
        usuarios.add(usuario)
        return true
    }

    /** Devuelve true y guarda la sesión si correo y contraseña coinciden. */
    fun iniciarSesion(correo: String, contrasena: String): Boolean {
        val usuario = usuarios.find {
            it.correo.equals(correo.trim(), ignoreCase = true) && it.contrasena == contrasena
        }
        usuarioActual = usuario
        return usuario != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // ---------- Especialidades ----------

    fun buscarEspecialidades(texto: String): List<Especialidad> {
        val consulta = texto.trim()
        if (consulta.isEmpty()) return especialidades
        return especialidades.filter { it.nombre.contains(consulta, ignoreCase = true) }
    }

    fun especialidadesDestacadas(): List<Especialidad> = especialidades.take(5)

    fun obtenerEspecialidad(id: Int): Especialidad? = especialidades.find { it.id == id }

    // ---------- Médicos ----------

    fun obtenerMedico(id: Int): Medico? = medicos.find { it.id == id }

    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> =
        medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }

    fun buscarMedicos(texto: String): List<Medico> {
        val consulta = texto.trim()
        return medicos
            .filter { it.nombre.contains(consulta, ignoreCase = true) }
            .sortedByDescending { it.calificacion }
    }

    // ---------- Citas ----------

    fun obtenerCita(id: Int): Cita? = citas.find { it.id == id }

    /** Horarios libres de un médico en una fecha: quita los ya reservados. */
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupados = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupados }
    }

    /** Crea la cita del usuario actual. Devuelve null si no hay sesión o el horario ya está tomado. */
    fun agendarCita(medicoId: Int, fecha: String, hora: String): Cita? {
        val usuario = usuarioActual ?: return null
        val ocupado = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (ocupado) return null
        val cita = Cita(siguienteCitaId++, usuario.correo, medicoId, fecha, hora)
        citas.add(cita)
        return cita
    }

    fun citasDelUsuario(): List<Cita> {
        val correo = usuarioActual?.correo ?: return emptyList()
        return citas
            .filter { it.correoPaciente == correo }
            .sortedWith(compareBy<Cita>({ it.fecha }, { it.hora }))
    }

    fun cancelarCita(id: Int): Boolean = citas.removeAll { it.id == id }
}
