package com.saludplus.citas.data.model

data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val sedeId: Int,
    val codigo: String,
    val telefono: String,
    val calificacion: Double,
    val experiencia: Int,
    val consultorio: String
)