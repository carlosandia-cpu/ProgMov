package com.andia.costocarrera

fun main() {

    print("Nombre del estudiante: ")
    val nombreEstudiante = readln()

    print("Cantidad de cursos: ")
    val cantidadCursos = readln().toInt()

    print("Valor de cada credito: ")
    val valorCredito = readln().toDouble()

    var totalCreditos = 0
    var totalPagar = 0.0
    var detalleCursos = ""

    for (i in 1..cantidadCursos) {

        println("\nCurso $i")

        print("Nombre del curso: ")
        val nombreCurso = readln()

        print("Cantidad de creditos: ")
        val creditos = readln().toInt()

        val costoCurso = creditos * valorCredito

        totalCreditos += creditos
        totalPagar += costoCurso

        detalleCursos += "%-20s %-10d S/ %.2f\n".format(
            nombreCurso,
            creditos,
            costoCurso
        )
    }

    val cargaAcademica = when {
        totalCreditos <= 12 -> "M.R (Malla Regular)"
        totalCreditos <= 18 -> "Carga Completa"
        else -> "Requiere Autorizacion"
    }

    val cantidadCuotas = if (totalPagar > 2500) {
        3
    } else {
        2
    }

    val montoCuota = totalPagar / cantidadCuotas

    println("\n==============================================")
    println("           RESULTADO DE MATRICULA")
    println("==============================================")
    println("Estudiante: $nombreEstudiante")
    println()

    println("%-20s %-10s %-10s".format("Curso", "Creditos", "Costo"))
    println("----------------------------------------------")

    print(detalleCursos)

    println("----------------------------------------------")
    println("RESUMEN")
    println("----------------------------------------------")
    println("Cursos matriculados: $cantidadCursos")
    println("Total de creditos: $totalCreditos")
    println("Total a pagar: S/ %.2f".format(totalPagar))
    println("Carga academica: $cargaAcademica")
    println(
        "Forma de pago: $cantidadCuotas cuotas de S/ %.2f".format(montoCuota)
    )
}