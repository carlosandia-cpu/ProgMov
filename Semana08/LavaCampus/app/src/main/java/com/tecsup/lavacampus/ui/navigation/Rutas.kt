package com.tecsup.lavacampus.ui.navigation

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"

    // Estudiante
    const val INICIO = "inicio"
    const val NUEVO_PEDIDO = "nuevo_pedido"
    const val MIS_PEDIDOS = "mis_pedidos"
    const val TARIFAS = "tarifas"
    const val PERFIL = "perfil"
    const val DETALLE = "detalle/{id}"
    fun detalle(id: Int) = "detalle/$id"

    // Administrador
    const val PANEL = "panel"
    const val BANDEJA = "bandeja"
    const val GESTION = "gestion/{id}"
    fun gestion(id: Int) = "gestion/$id"
    const val SERVICIOS = "servicios"
    const val FORMULARIO = "formulario/{id}"
    fun formulario(id: Int) = "formulario/$id"
    const val CLIENTES = "clientes"
    const val AJUSTES = "ajustes"
}