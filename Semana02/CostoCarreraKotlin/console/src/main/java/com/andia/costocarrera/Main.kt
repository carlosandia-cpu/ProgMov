package com.andia.costocarrera

fun main() {

    print("Nombre del estudiante: ")
    val nombreEstudiante = readln()

    print("Cantidad de cursos: ")
    val cantidadCursos = readln().toInt()

    print("Valor de cada credito: ")
    val valorCredito = readln().toDouble()

    val nombresCursos = mutableListOf<String>()
    val creditosCursos = mutableListOf<Int>()
    val costosCursos = mutableListOf<Double>()

    for (i in 1..cantidadCursos) {

        println("\nCurso $i")

        print("Nombre del curso: ")
        val nombreCurso = readln()
        nombresCursos.add(nombreCurso)

        print("Cantidad de creditos: ")
        val creditos = readln().toInt()
        creditosCursos.add(creditos)

        // Costo individual del curso
        val costoCurso = creditos * valorCredito
        costosCursos.add(costoCurso)
    }

    // Total de creditos
    val totalCreditos = creditosCursos.sum()

    // Total a pagar
    val totalPagar = costosCursos.sum()

    // Carga academica
    val cargaAcademica = when {
        totalCreditos <= 12 -> "M.R (Malla Regular)"
        totalCreditos <= 18 -> "Carga Completa"
        else -> "Requiere Autorizacion"
    }

    // Cantidad de cuotas
    val cantidadCuotas = if (totalPagar > 2500) {
        3
    } else {
        2
    }

    // Valor de cada cuota
    val montoCuota = totalPagar / cantidadCuotas

    println("\nCalculos realizados correctamente.")
}