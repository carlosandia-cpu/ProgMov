package com.tecsup.lavacampus.model

object Casilleros {
    val todos: List<String> = (1..30).map { "C-%02d".format(it) }

    fun esValido(c: String): Boolean = c in todos
}