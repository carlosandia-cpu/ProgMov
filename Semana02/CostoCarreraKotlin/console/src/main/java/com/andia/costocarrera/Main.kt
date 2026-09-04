package com.andia.costocarrera

fun leerTextoValido(mensaje: String): String {
    while (true) {
        print(mensaje)
        val texto = readln().trim()

        if (texto.isNotEmpty() && texto.any { it.isLetter() } && texto.none { it.isDigit() }) {
            return texto
        }

        println("Entrada no válida. Ingresa un texto válido sin números.")
    }
}

fun leerEnteroPositivo(mensaje: String): Int {
    while (true) {
        print(mensaje)

        val entrada = readln().trim()
        val numero = entrada.toIntOrNull()

        if (numero != null && numero > 0) {
            return numero
        }

        println("Entrada no válida. Ingresa un número entero mayor que 0.")
    }
}

fun leerDoublePositivo(mensaje: String): Double {
    while (true) {
        print(mensaje)

        val numero = readln().toDoubleOrNull()

        if (numero != null && numero > 0) {
            return numero
        }

        println("Entrada no válida. Ingresa un valor numérico mayor que 0.")
    }
}

fun seleccionarTurno(): Pair<String, Double> {

    while (true) {

        println("\nSeleccione el turno:")
        println("1. Mañana - 10% adicional")
        println("2. Tarde - 15% adicional")
        println("3. Noche - 20% adicional")

        print("Opción: ")

        when (readln()) {
            "1" -> return Pair("Mañana", 0.10)
            "2" -> return Pair("Tarde", 0.15)
            "3" -> return Pair("Noche", 0.20)
            else -> println("Opción no válida. Selecciona 1, 2 o 3.")
        }
    }
}

fun main() {

    val nombreEstudiante =
        leerTextoValido("Nombre del estudiante: ")

    val cantidadCursos =
        leerEnteroPositivo("Cantidad de cursos: ")

    val valorCredito =
        leerDoublePositivo("Valor de cada crédito: ")

    var totalCreditos = 0
    var totalPagar = 0.0
    var detalleCursos = ""

    for (i in 1..cantidadCursos) {

        println("\nCurso $i")

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

    val (turno, porcentajeTurno) = seleccionarTurno()

    val recargoTurno = totalPagar * porcentajeTurno
    val totalConTurno = totalPagar + recargoTurno

    val cargaAcademica = when {
        totalCreditos <= 12 -> "M.R (Malla Regular)"
        totalCreditos <= 18 -> "Carga Completa"
        else -> "Sobrecarga Académica"
    }

    println("\n========== MATRÍCULA ==========")
    println("Estudiante: $nombreEstudiante")
    println()
    println("%-20s %-10s %-15s".format("Curso", "Créditos", "Costo"))
    println("---------------------------------------------")
    print(detalleCursos)

    println("\nCursos matriculados: $cantidadCursos")
    println("Total de créditos: $totalCreditos")
    println("Carga académica: $cargaAcademica")

    println("\nTurno seleccionado: $turno")
    println("Monto base: S/ %.2f".format(totalPagar))
    println("Recargo por turno: S/ %.2f".format(recargoTurno))
    println("Total con turno: S/ %.2f".format(totalConTurno))
}