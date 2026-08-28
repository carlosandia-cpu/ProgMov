package com.andia.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0

    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }

    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

fun mostrarDetalle(productos: List<Producto>) {
    println("--------- DETALLE DEL CARRITO ---------")

    var i = 1

    for (p in productos) {
        val importe = p.precio * p.cantidad

        println(
            String.format(
                "%d. %-20s x%d S/ %8.2f",
                i,
                p.nombre,
                p.cantidad,
                importe
            )
        )

        i++
    }

    println("---------------------------------------")
}

fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

fun buscarProducto(
    productos: List<Producto>,
    nombre: String
): Producto? {
    return productos.find {
        it.nombre.equals(nombre, ignoreCase = true)
    }
}

fun eliminarProducto(
    productos: MutableList<Producto>,
    nombre: String
): Boolean {
    return productos.removeIf {
        it.nombre.equals(nombre, ignoreCase = true)
    }
}

fun main() {

    println("=========================================")
    println(" CARRITO DE COMPRAS - TIENDA TECSUP ")
    println("=========================================")

    val nombreCliente = "Carlos Andia"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente")
    println()

    carrito.add(Producto("Laptop HP", 2500.0, 1))
    carrito.add(Producto("Mouse Logitech", 45.5, 2))
    carrito.add(Producto("Audifonos Sony", 120.0, 1))
    carrito.add(Producto("Teclado Redragon", 180.0, 2))

    for (producto in carrito) {
        println("Producto agregado: ${producto.nombre}")
    }

    println()

    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    mostrarDetalle(carrito)

    println("Cantidad de productos: ${carrito.size}")

    println()

    println(String.format("Subtotal      : S/ %8.2f", subtotal))
    println(String.format("IGV (18%%)     : S/ %8.2f", igv))
    println(String.format("TOTAL A PAGAR : S/ %8.2f", total))

    println()

    val masCaro = carrito.maxByOrNull { it.precio }

    if (masCaro != null) {
        println(
            "Producto mas caro: ${masCaro.nombre} " +
                    String.format("(S/ %.2f)", masCaro.precio)
        )
    }

    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    println()

    println(String.format("Descuento aplicado: S/ %.2f", descuento))
    println(String.format("TOTAL CON DESCUENTO: S/ %.2f", totalConDescuento))


    println()
    println("========== RETO ADICIONAL ==========")

    while (true) {

        println()
        println("1. Buscar producto")
        println("2. Eliminar producto")
        println("3. Agregar producto")
        println("0. Salir")
        print("Seleccione una opcion: ")

        when (readLine()) {

            "1" -> {
                print("Ingrese el nombre del producto a buscar: ")
                val nombreBuscado = readLine() ?: ""

                val productoBuscado = buscarProducto(carrito, nombreBuscado)

                if (productoBuscado != null) {
                    println()
                    println("Producto encontrado: ${productoBuscado.nombre}")
                    println(String.format("Precio: S/ %.2f", productoBuscado.precio))
                    println("Cantidad: ${productoBuscado.cantidad}")
                } else {
                    println("Producto no encontrado.")
                }
            }

            "2" -> {
                print("Ingrese el nombre del producto a eliminar: ")
                val nombreEliminar = readLine() ?: ""

                val eliminado = eliminarProducto(carrito, nombreEliminar)

                if (eliminado) {
                    println("Producto eliminado correctamente.")

                    println()
                    mostrarDetalle(carrito)

                    val nuevoSubtotal = calcularSubtotal(carrito)
                    val nuevoIGV = calcularIGV(nuevoSubtotal)
                    val nuevoTotal = calcularTotal(nuevoSubtotal, nuevoIGV)
                    val nuevoDescuento = calcularDescuento(nuevoTotal)
                    val nuevoTotalConDescuento =
                        nuevoTotal - nuevoDescuento

                    println("Cantidad de productos: ${carrito.size}")
                    println()

                    println(
                        String.format(
                            "Nuevo subtotal       : S/ %8.2f",
                            nuevoSubtotal
                        )
                    )

                    println(
                        String.format(
                            "Nuevo IGV (18%%)      : S/ %8.2f",
                            nuevoIGV
                        )
                    )

                    println(
                        String.format(
                            "Nuevo total          : S/ %8.2f",
                            nuevoTotal
                        )
                    )

                    println(
                        String.format(
                            "Nuevo descuento      : S/ %8.2f",
                            nuevoDescuento
                        )
                    )

                    println(
                        String.format(
                            "TOTAL ACTUALIZADO    : S/ %8.2f",
                            nuevoTotalConDescuento
                        )
                    )

                } else {
                    println("No se encontro el producto.")
                }
            }

            "3" -> {
                print("Nombre del producto: ")
                val nombre = readLine() ?: ""

                print("Precio del producto: ")
                val precio = readLine()?.toDoubleOrNull()

                print("Cantidad: ")
                val cantidad = readLine()?.toIntOrNull()

                if (
                    nombre.isNotBlank() &&
                    precio != null &&
                    cantidad != null &&
                    precio > 0 &&
                    cantidad > 0
                ) {
                    carrito.add(
                        Producto(
                            nombre,
                            precio,
                            cantidad
                        )
                    )

                    println("Producto agregado correctamente.")

                    println()
                    mostrarDetalle(carrito)

                } else {
                    println("Datos invalidos.")
                }
            }

            "0" -> {
                println()
                println("Saliendo del programa...")
                break
            }

            else -> {
                println("Opcion no valida.")
            }
        }
    }
}