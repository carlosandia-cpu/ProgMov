package com.example.tecsupfit.model

data class Reserva(
    val clase: ClaseGimnasio,
    val horario: String,
    val estado: String = "Confirmada"
)