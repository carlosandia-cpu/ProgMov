package com.saludplus.citas.ui.components

/** Convierte "2026-10-08" en "08/10/2026". */
fun formatearFecha(iso: String): String {
    val partes = iso.split("-")
    return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else iso
}