package com.saludplus.citas.data.model

data class Usuario(
    val nombre: String,
    val correo: String,
    val contrasena: String,
    val telefono: String = ""
)
