package com.tecsup.avisoscampus.model

enum class Origen(val texto: String) {
    BOTON("Botón"),
    RECORDATORIO("Recordatorio"),
    MODO_AVION("Modo avión"),
    CARGADOR("Cargador");

    val esUrgente: Boolean
        get() = this == RECORDATORIO
}

data class Aviso(
    val id: Int,
    val titulo: String,
    val texto: String,
    val origen: Origen,
    val hora: String
) {
    val esUrgente: Boolean
        get() = origen.esUrgente
}

sealed interface PermisoUi {
    data object SinPedir : PermisoUi
    data object Concedido : PermisoUi
    data object Denegado : PermisoUi
}