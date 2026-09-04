package com.andia.costocarrera

fun leerTextoValido(mensaje: String): String {
    while (true) {
        print(mensaje)

        val texto = readln().trim()

        if (
            texto.isNotEmpty() &&
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
        println("Seleccione una opción:")
        println("1. Ordinario")
        println("2. Becado")
        print("Opción: ")

        when (readln().trim().lowercase()) {
            "1", "ordinario" -> return "Ordinario"
            "2", "becado" -> return "Becado"
            else -> println(
                "Opción no válida. Ingresa 1 u Ordinario, o 2 o Becado."
            )
        }
    }
}

fun seleccionarTurno(): Pair<String, Double> {
    while (true) {

        println("\n========== TURNO ==========")
        println("Seleccione una opción:")
        println("1. Mañana - 10% adicional")
        println("2. Tarde  - 15% adicional")
        println("3. Noche  - 20% adicional")
        print("Opción: ")

        when (readln().trim().lowercase()) {
            "1", "mañana", "manana" ->
                return Pair("Mañana", 0.10)

            "2", "tarde" ->
                return Pair("Tarde", 0.15)

            "3", "noche" ->
                return Pair("Noche", 0.20)

            else ->
                println(
                    "Opción no válida. Selecciona Mañana, Tarde o Noche."
                )
        }
    }
}

fun registrarEstudiante(numeroRegistro: Int) {

    println("\n================================")
    println("          REGISTRO N° $numeroRegistro")
    println("================================")

    val nombreEstudiante =
        leerTextoValido("Nombre del estudiante: ")

    // CATEGORÍA
    val categoria = seleccionarCategoria()

    val montoMatricula = if (categoria == "Ordinario") {

        leerDoublePositivo(
            "Ingrese el monto de matrícula: S/ "
        )

    } else {

        println("Monto de matrícula para becado: S/ 0.00")
        0.0
    }

    // TURNO
    val (turno, porcentajeTurno) =
        seleccionarTurno()

    println("\n========== DATOS ACADÉMICOS ==========")

    val cantidadCursos =
        leerEnteroPositivo("Cantidad de cursos: ")

    val valorCredito =
        leerDoublePositivo("Valor de cada crédito: S/ ")

    var totalCreditos = 0
    var totalCursos = 0.0
    var detalleCursos = ""

    for (i in 1..cantidadCursos) {

        println("\n---------- CURSO $i ----------")

        val nombreCurso =
            leerTextoValido("Nombre del curso: ")

        val creditos =
            leerEnteroPositivo("Cantidad de créditos: ")

        val costoCurso =
            creditos * valorCredito

        totalCreditos += creditos
        totalCursos += costoCurso

        detalleCursos +=
            "%-20s %-10d S/ %.2f\n".format(
                nombreCurso,
                creditos,
                costoCurso
            )
    }

    // MONTO BASE
    val montoBase =
        montoMatricula + totalCursos

    // RECARGO POR TURNO
    val recargoTurno =
        montoBase * porcentajeTurno

    val totalConTurno =
        montoBase + recargoTurno

    // IGV
    val igv =
        totalConTurno * 0.18

    // TOTAL FINAL
    val totalFinal =
        totalConTurno + igv

    // CUOTAS
    val cantidadCuotas = 2
    val montoCuota =
        totalFinal / cantidadCuotas

    // CARGA ACADÉMICA
    val cargaAcademica = when {
        totalCreditos <= 12 ->
            "M.R (Malla Regular)"

        totalCreditos <= 18 ->
            "Carga Completa"

        else ->
            "Sobrecarga Académica"
    }

    // RESULTADO
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

    println("\n---------- DATOS ACADÉMICOS ----------")

    println("Cursos matriculados : $cantidadCursos")
    println("Total de créditos   : $totalCreditos")
    println("Carga académica     : $cargaAcademica")

    println("\n---------- PAGO ----------")

    println(
        "Matrícula           : S/ %.2f".format(
            montoMatricula
        )
    )

    println(
        "Costo de cursos     : S/ %.2f".format(
            totalCursos
        )
    )

    println(
        "Monto base          : S/ %.2f".format(
            montoBase
        )
    )

    println(
        "Recargo por turno   : S/ %.2f".format(
            recargoTurno
        )
    )

    println(
        "Monto con turno     : S/ %.2f".format(
            totalConTurno
        )
    )

    println(
        "IGV (18%%)           : S/ %.2f".format(
            igv
        )
    )

    println(
        "TOTAL FINAL         : S/ %.2f".format(
            totalFinal
        )
    )

    println("\n---------- CUOTAS ----------")

    println(
        "Forma de pago       : $cantidadCuotas cuotas"
    )

    println(
        "Monto por cuota     : S/ %.2f".format(
            montoCuota
        )
    )

    println("================================")
}

fun main() {

    println("================================")
    println("        SISTEMA DE MATRÍCULA")
    println("================================")

    val aforoMaximo =
        leerEnteroPositivo(
            "Ingrese el aforo máximo: "
        )

    var estudiantesRegistrados = 0

    while (true) {

        if (estudiantesRegistrados >= aforoMaximo) {

            println("\n================================")
            println("          AFORO COMPLETO")
            println("================================")
            println("Capacidad máxima       : $aforoMaximo")
            println("Estudiantes registrados: $estudiantesRegistrados")
            println("No se pueden registrar más estudiantes.")
            println("================================")

            break
        }

        registrarEstudiante(
            estudiantesRegistrados + 1
        )

        estudiantesRegistrados++

        println(
            "\nAforo actual: " +
                    "$estudiantesRegistrados / $aforoMaximo"
        )

        if (estudiantesRegistrados >= aforoMaximo) {
            continue
        }

        println("\n¿Desea registrar otro estudiante?")
        println("Seleccione una opción:")
        println("1. Sí")
        println("0. No")
        print("Opción: ")

        var opcion: String

        while (true) {

            opcion = readln().trim().lowercase()

            if (
                opcion == "1" ||
                opcion == "sí" ||
                opcion == "si" ||
                opcion == "0" ||
                opcion == "no"
            ) {
                break
            }

            print(
                "Opción no válida. Ingresa 1 o Sí para continuar, " +
                        "0 o No para finalizar: "
            )
        }

        if (
            opcion == "0" ||
            opcion == "no"
        ) {

            println("\nRegistro finalizado.")

            println(
                "Estudiantes registrados: " +
                        "$estudiantesRegistrados / $aforoMaximo"
            )

            break
        }
    }
}