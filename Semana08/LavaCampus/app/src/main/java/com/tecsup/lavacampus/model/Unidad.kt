package com.tecsup.lavacampus.model

enum class Unidad(
    val etiqueta: String,
    val texto: String,
    val paso: Double,
    val minimo: Double,
    val maximo: Double
) {
    KG("Kilo", "por kg", 0.5, 1.0, 10.0),
    PRENDA("Prenda", "por prenda", 1.0, 1.0, 30.0),
    UNIDAD("Unidad", "por unidad", 1.0, 1.0, 30.0);

    companion object {
        fun desde(valor: String): Unidad = valueOf(valor)
    }
}