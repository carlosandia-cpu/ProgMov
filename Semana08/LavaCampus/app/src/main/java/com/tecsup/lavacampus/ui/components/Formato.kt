package com.tecsup.lavacampus.ui.components

import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.model.Unidad
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Double.soles(): String = "S/ " + String.format(Locale.US, "%.2f", this)

fun Double.cantidadTexto(unidad: String): String = when (unidad) {
    Unidad.KG.name -> String.format(Locale.US, "%.1f kg", this)
    Unidad.PRENDA.name -> "${toInt()} ${if (toInt() == 1) "prenda" else "prendas"}"
    else -> "${toInt()} unid."
}

/** 'S/ 8.00 por kg' */
fun Servicio.textoPrecio(): String = "${precio.soles()} ${Unidad.desde(unidad).texto}"

fun String.fechaCorta(): String = runCatching {
    LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd MMM", Locale("es", "PE")))
}.getOrDefault(this)