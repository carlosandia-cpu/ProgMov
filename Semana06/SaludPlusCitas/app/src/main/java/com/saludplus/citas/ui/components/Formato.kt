package com.saludplus.citas.ui.components

import java.time.LocalDate

private val diasSemana = listOf(
    "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
)

private val meses = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "setiembre", "octubre", "noviembre", "diciembre"
)

/** Convierte "2026-10-08" en "08/10/2026". */
fun formatearFecha(iso: String): String {
    val partes = iso.split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else iso
}

/** Convierte "2026-10-08" en "Jueves 8 de octubre 2026". */
fun formatearFechaLarga(iso: String): String {
    return try {
        val fecha = LocalDate.parse(iso)
        val nombreDia = diasSemana[fecha.dayOfWeek.value - 1]
        val nombreMes = meses[fecha.monthValue - 1]
        "$nombreDia ${fecha.dayOfMonth} de $nombreMes ${fecha.year}"
    } catch (_: Exception) {
        iso
    }
}
