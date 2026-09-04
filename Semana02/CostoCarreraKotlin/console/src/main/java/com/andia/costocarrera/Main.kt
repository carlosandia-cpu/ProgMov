package com.andia.costocarrera

fun leerTextoValido(mensaje: String): String {
    while (true) {
        print(mensaje)
        val texto = readln().trim()

        if (texto.isNotEmpty() &&
            texto.any { it.isLetter() } &&
            texto.none { it.isDigit() }
        ) {
            return texto
        }

        println("Entrada no válida. Ingresa únicamente texto.")
    }
}

fun leerEnteroPositivo(mensaje: String): Int {
    while (true) {
        print(mensaje)

        val numero = readln().trim().toIntOrNull()

        if (numero != null && numero > 0) {
            return numero
        }

        println("Entrada no válida. Ingresa un número entero mayor que 0.")
    }
}

fun leerDoublePositivo(mensaje: String): Double {
    while (true) {
        print(mensaje)

        val numero = readln().trim().toDoubleOrNull()

        if (numero != null && numero > 0) {
            return numero
        }

        println("Entrada no válida. Ingresa un valor numérico mayor que 0.")
    }
}

fun seleccionarCategoria(): String {
    while (true) {

        println("\n========== CATEGORÍA ==========")
        println("1. Ordinario")
        println("2. Becado")
        print("Seleccione una opción: ")

        when (readln().trim()) {
            "1" -> return "Ordinario"
            "2" -> return "Becado"
            else -> println("Opción no válida. Selecciona 1 o 2.")
        }
    }
}

fun seleccionarTurno(): Pair<String, Double> {
    while (true) {

        println("\n========== TURNO ==========")
        println("1. Mañana - 10% adicional")
        println("2. Tarde  - 15% adicional")
        println("3. Noche  - 20% adicional")
        print("Seleccione una opción: ")

        when (readln().trim()) {
            "1" -> return Pair("Mañana", 0.10)
            "2" -> return Pair("Tarde", 0.15)
            "3" -> return Pair("Noche", 0.20)
            else -> println("Opción no válida. Selecciona 1, 2 o 3.")
        }
    }
}

fun main() {

    println("================================")
    println("       COSTO DE MATRÍCULA")
    println("================================")

    val nombreEstudiante =
        leerTextoValido("Nombre del estudiante: ")

    val categoria = seleccionarCategoria()

    if (categoria == "Becado") {

        println("\n================================")
        println("       RESUMEN DE MATRÍCULA")
        println("================================")
        println("Estudiante : $nombreEstudiante")
        println("Categoría  : $categoria")
        println("Matrícula  : S/ 0.00")
        println("================================")

        return
    }

    val (turno, porcentajeTurno) = seleccionarTurno()

    println("\n========== DATOS ACADÉMICOS ==========")

    val cantidadCursos =
        leerEnteroPositivo("Cantidad de cursos: ")

    val valorCredito =
        leerDoublePositivo("Valor de cada crédito: ")

    var totalCreditos = 0
    var totalPagar = 0.0
    var detalleCursos = ""

    for (i in 1..cantidadCursos) {

        println("\n----- Curso $i -----")

        val nombreCurso =
            leerTextoValido("Nombre del curso: ")

        val creditos =
            leerEnteroPositivo("Cantidad de créditos: ")

        val costoCurso = creditos * valorCredito

        totalCreditos += creditos
        totalPagar += costoCurso

        detalleCursos += "%-20s %-10d S/ %.2f\n".format(
            nombreCurso,
            creditos,
            costoCurso
        )
    }

    val recargoTurno = totalPagar * porcentajeTurno
    val totalConTurno = totalPagar + recargoTurno

    val igv = totalConTurno * 0.18
    val matriculaFinal = totalConTurno + igv

    val cargaAcademica = when {
        totalCreditos <= 12 -> "M.R (Malla Regular)"
        totalCreditos <= 18 -> "Carga Completa"
        else -> "Sobrecarga Académica"
    }

    println("\n================================")
    println("       RESUMEN DE MATRÍCULA")
    println("================================")

    println("Estudiante : $nombreEstudiante")
    println("Categoría  : $categoria")
    println("Turno      : $turno")

    println("\n---------- CURSOS ----------")
    println(
        "%-20s %-10s %-15s".format(
            "Curso",
            "Créditos",
            "Costo"
        )
    )

    println("---------------------------------------------")
    print(detalleCursos)

    println("\n---------- RESUMEN ----------")
    println("Cursos matriculados : $cantidadCursos")
    println("Total de créditos   : $totalCreditos")
    println("Carga académica     : $cargaAcademica")

    println("\n---------- PAGO ----------")
    println("Monto base          : S/ %.2f".format(totalPagar))
    println("Recargo por turno   : S/ %.2f".format(recargoTurno))
    println("Monto con turno     : S/ %.2f".format(totalConTurno))
    println("IGV (18%%)           : S/ %.2f".format(igv))
    println("Matrícula final     : S/ %.2f".format(matriculaFinal))

    println("================================")
}