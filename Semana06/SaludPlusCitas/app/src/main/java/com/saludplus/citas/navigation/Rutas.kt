package com.saludplus.citas.navigation

import android.net.Uri

object Rutas {
    // Nombres de los argumentos
    const val ARG_ESPECIALIDAD_ID = "especialidadId"
    const val ARG_MEDICO_ID = "medicoId"
    const val ARG_FECHA = "fecha"
    const val ARG_HORA = "hora"
    const val ARG_CITA_ID = "citaId"

    // Rutas sin parámetros
    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"
    const val TERMINOS = "terminos"
    const val HOME = "home"
    const val ESPECIALIDADES = "especialidades"
    const val MIS_CITAS = "mis_citas"
    const val RESULTADOS = "resultados"
    const val PERFIL = "perfil"
    const val NOTIFICACIONES = "notificaciones"

    // Rutas con parámetros (plantillas para el NavHost)
    const val MEDICOS = "medicos/{$ARG_ESPECIALIDAD_ID}"
    const val FECHA_HORA = "fecha_hora/{$ARG_MEDICO_ID}"
    const val CONFIRMAR = "confirmar/{$ARG_MEDICO_ID}/{$ARG_FECHA}/{$ARG_HORA}"
    const val CITA_EXITOSA = "cita_exitosa/{$ARG_CITA_ID}"
    const val DETALLE_CITA = "detalle_cita/{$ARG_CITA_ID}"

    // Funciones para navegar con valores reales
    fun medicos(especialidadId: Int) = "medicos/$especialidadId"
    fun fechaHora(medicoId: Int) = "fecha_hora/$medicoId"
    fun confirmar(medicoId: Int, fecha: String, hora: String) =
        "confirmar/$medicoId/$fecha/${Uri.encode(hora)}"
    fun citaExitosa(citaId: Int) = "cita_exitosa/$citaId"
    fun detalleCita(citaId: Int) = "detalle_cita/$citaId"
}
