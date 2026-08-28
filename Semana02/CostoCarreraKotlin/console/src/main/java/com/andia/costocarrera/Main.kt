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

    for (i in 1..cantidadCursos) {

        println("\nCurso $i")

        print("Nombre del curso: ")
        val nombreCurso = readln()
        nombresCursos.add(nombreCurso)

        print("Cantidad de creditos: ")
        val creditos = readln().toInt()
        creditosCursos.add(creditos)
    }

    println("\nDatos ingresados correctamente.")
}