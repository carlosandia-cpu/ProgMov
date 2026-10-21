package com.tecsup.lavacampus.model

/** Resultado de la consulta GROUP BY del panel. Los nombres deben coincidir con los alias del SQL. */
data class IngresoServicio(
    val nombre: String,
    val total: Double
)