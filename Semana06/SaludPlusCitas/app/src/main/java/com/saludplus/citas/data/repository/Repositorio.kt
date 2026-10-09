package com.saludplus.citas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.saludplus.citas.data.model.Cita
import com.saludplus.citas.data.model.Especialidad
import com.saludplus.citas.data.model.Medico
import com.saludplus.citas.data.model.Sede
import com.saludplus.citas.data.model.Usuario
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

/**
 * Único repositorio de la app (object = una sola instancia compartida).
 * Todo vive en memoria: no hay base de datos y se pierde al cerrar la app.
 */
object Repositorio {

    // ---------- Colecciones ----------

    val usuarios = mutableStateListOf(
        Usuario("Paciente Demo", "paciente@saludplus.pe", "123456", "999888777")
    )

    val sedes = listOf(
        Sede(1, "SaludPlus San Isidro", "Av. Javier Prado Este 1250", "San Isidro", "01 415 2200", "Lun a Sáb · 7:00 a 20:00"),
        Sede(2, "SaludPlus Miraflores", "Av. Larco 890", "Miraflores", "01 445 3100", "Lun a Sáb · 7:00 a 20:00"),
        Sede(3, "SaludPlus La Molina", "Av. La Molina 2450", "La Molina", "01 348 5600", "Lun a Vie · 8:00 a 19:00"),
        Sede(4, "SaludPlus Surco", "Av. Primavera 1830", "Santiago de Surco", "01 372 4800", "Lun a Sáb · 8:00 a 19:00")
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

    // Medico(id, nombre, especialidadId, sedeId, codigo, telefono, calificacion, experiencia, consultorio)
    val medicos = listOf(
        Medico(1, "Dra. Ana Torres", 1, 1, "CMP-30451", "987 301 451", 4.9, 12, "Consultorio 301"),
        Medico(2, "Dr. Carlos Mendoza", 1, 2, "CMP-28764", "987 287 640", 4.6, 8, "Consultorio 302"),
        Medico(3, "Dr. Luis Vega", 2, 1, "CMP-31022", "987 310 220", 4.7, 10, "Consultorio 201"),
        Medico(4, "Dra. Marta Quispe", 2, 3, "CMP-27519", "987 275 190", 4.8, 15, "Consultorio 202"),
        Medico(5, "Dra. Rosa Díaz", 3, 2, "CMP-29833", "987 298 330", 4.8, 9, "Consultorio 105"),
        Medico(6, "Dr. Jorge Paredes", 3, 3, "CMP-33140", "987 331 400", 4.5, 6, "Consultorio 106"),
        Medico(7, "Dr. Pablo Ramos", 4, 1, "CMP-24608", "987 246 080", 4.9, 18, "Consultorio 401"),
        Medico(8, "Dra. Elena Castro", 5, 2, "CMP-30977", "987 309 770", 4.6, 11, "Consultorio 110"),
        Medico(9, "Dr. Andrés Salas", 5, 4, "CMP-34215", "987 342 150", 4.4, 5, "Consultorio 111"),
        Medico(10, "Dra. Lucía Fernández", 6, 1, "CMP-28341", "987 283 410", 4.7, 13, "Consultorio 205"),
        Medico(11, "Dra. Sofía Núñez", 7, 2, "CMP-29106", "987 291 060", 4.8, 14, "Consultorio 305"),
        Medico(12, "Dr. Miguel Rojas", 8, 4, "CMP-32788", "987 327 880", 4.5, 7, "Consultorio 101"),
        Medico(13, "Dr. Iván Cárdenas", 8, 1, "CMP-35402", "987 354 020", 4.6, 9, "Consultorio 102"),
        Medico(14, "Dra. Valeria Soto", 2, 3, "CMP-33907", "987 339 070", 4.7, 8, "Consultorio 203")
    )

    // Horarios de atención de todos los médicos
    val horariosBase = listOf(
        "08:00", "09:00", "10:00", "11:00",
        "14:00", "15:00", "16:00", "17:00"
    )

    // Próximo día hábil: sirve para dejar horarios ya ocupados de ejemplo
    private fun proximoDiaHabil(): LocalDate {
        var d = LocalDate.now().plusDays(1)
        while (d.dayOfWeek == DayOfWeek.SATURDAY || d.dayOfWeek == DayOfWeek.SUNDAY) {
            d = d.plusDays(1)
        }
        return d
    }

    private val diaDemo = proximoDiaHabil().toString()

    // Citas de ejemplo: dos horarios ocupados de la Dra. Torres y una cita completada del usuario demo.
    val citas = mutableStateListOf(
        Cita(1, "otro@correo.com", 1, diaDemo, "08:00"),
        Cita(2, "otro@correo.com", 1, diaDemo, "09:00"),
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

    // ---------- Sedes ----------

    fun obtenerSede(id: Int): Sede? = sedes.find { it.id == id }

    /** Doctores de una sede, de mejor a peor calificación. */
    fun medicosPorSede(sedeId: Int): List<Medico> =
        medicos
            .filter { it.sedeId == sedeId }
            .sortedByDescending { it.calificacion }

    /** Especialidades que tienen al menos un doctor en esa sede. */
    fun especialidadesDeSede(sedeId: Int): List<Especialidad> =
        especialidades.filter { esp ->
            medicos.any { it.sedeId == sedeId && it.especialidadId == esp.id }
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

    /**
     * Doctores agrupados por especialidad (solo las que tienen doctores).
     * sedeId = null trae todas las sedes; texto filtra por nombre del doctor.
     */
    fun doctoresPorEspecialidad(
        sedeId: Int? = null,
        texto: String = ""
    ): List<Pair<Especialidad, List<Medico>>> {
        val consulta = texto.trim()
        return especialidades.mapNotNull { esp ->
            val lista = medicos
                .filter { m ->
                    m.especialidadId == esp.id &&
                            (sedeId == null || m.sedeId == sedeId) &&
                            (consulta.isEmpty() || m.nombre.contains(consulta, ignoreCase = true))
                }
                .sortedByDescending { it.calificacion }
            if (lista.isEmpty()) null else esp to lista
        }
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

    /**
     * Primer día hábil con horario libre para ese médico (fecha ISO, hora).
     * Hoy solo cuentan las horas que aún no pasaron.
     */
    fun proximoHorarioDisponible(medicoId: Int, diasABuscar: Int = 21): Pair<String, String>? {
        val hoy = LocalDate.now()
        val ahora = LocalTime.now()
        var dia = hoy
        repeat(diasABuscar) {
            if (dia.dayOfWeek != DayOfWeek.SATURDAY && dia.dayOfWeek != DayOfWeek.SUNDAY) {
                val libres = horariosDisponibles(medicoId, dia.toString())
                    .filter { dia != hoy || LocalTime.parse(it).isAfter(ahora) }
                if (libres.isNotEmpty()) return dia.toString() to libres.first()
            }
            dia = dia.plusDays(1)
        }
        return null
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