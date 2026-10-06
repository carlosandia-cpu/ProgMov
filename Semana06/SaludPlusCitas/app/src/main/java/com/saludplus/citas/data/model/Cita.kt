package com.saludplus.citas.data.model

// fecha en formato ISO "2026-10-08" y hora "09:00"
data class Cita(
    val id: Int,
    val correoPaciente: String,
    val medicoId: Int,
    val fecha: String,
    val hora: String,
    val estado: String = "Confirmada"
)
