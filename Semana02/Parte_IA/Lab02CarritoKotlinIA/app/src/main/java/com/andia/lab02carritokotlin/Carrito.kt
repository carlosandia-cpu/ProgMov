package com.andia.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    return productos.sumOf { it.precio * it.cantidad }
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun mostrarDetalle(productos: List<Producto>) {
    println("------------ DETALLE DEL CARRITO ------------")

    productos.forEachIndexed { indice, producto ->
        val importe = producto.precio * producto.cantidad

        println(
            String.format(
                "%d. %-20s x%d  S/ %8.2f",
                indice + 1,
                producto.nombre,
                producto.cantidad,
                importe
            )
        )
    }

    println("---------------------------------------------")
}

fun main() {
    println("=============================================")
    println("     CARRITO DE COMPRAS - VERSION IA")
    println("=============================================")

    val cliente = "Carlos Andia"

    val carrito = mutableListOf(
        Producto("Laptop Lenovo", 2800.0, 1),
        Producto("Mouse Logitech", 80.0, 2),
        Producto("Teclado Mecanico", 220.0, 1),
        Producto("Audifonos HyperX", 300.0, 1)
    )

    println("Cliente: $cliente")
    println()

    mostrarDetalle(carrito)

    println("Cantidad de productos: ${carrito.size}")
    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)
    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    println(String.format("Subtotal             : S/ %8.2f", subtotal))
    println(String.format("IGV (18%%)            : S/ %8.2f", igv))
    println(String.format("Total                : S/ %8.2f", total))

    println()

    val productoMasCaro = carrito.maxByOrNull { it.precio }

    if (productoMasCaro != null) {
        println(
            String.format(
                "Producto mas caro     : %s (S/ %.2f)",
                productoMasCaro.nombre,
                productoMasCaro.precio
            )
        )
    }

    val porcentajeDescuento = when {
        total > 5000 -> 10
        total > 3000 -> 5
        else -> 0
    }

    println(
        String.format(
            "Descuento aplicado (%d%%): S/ %8.2f",
            porcentajeDescuento,
            descuento
        )
    )

    println(
        String.format(
            "TOTAL CON DESCUENTO   : S/ %8.2f",
            totalConDescuento
        )
    )
}