package com.tecsup.lavacampus.model

/** Resultado de la consulta con JOIN de la pantalla Clientes. */
data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val cantidadPedidos: Int,
    val totalGastado: Double
)