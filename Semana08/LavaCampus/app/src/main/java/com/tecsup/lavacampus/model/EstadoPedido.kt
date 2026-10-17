package com.tecsup.lavacampus.model

import androidx.compose.ui.graphics.Color

enum class EstadoPedido(val etiqueta: String, val color: Color) {
    RECIBIDO("Recibido", Color(0xFFD97706)),
    EN_PROCESO("En proceso", Color(0xFF2563EB)),
    LISTO("Listo", Color(0xFF16A34A)),
    ENTREGADO("Entregado", Color(0xFF475569)),
    CANCELADO("Cancelado", Color(0xFFDC2626));

    /** Solo se avanza un paso a la vez; ENTREGADO y CANCELADO son finales. */
    fun siguiente(): EstadoPedido? = when (this) {
        RECIBIDO -> EN_PROCESO
        EN_PROCESO -> LISTO
        LISTO -> ENTREGADO
        else -> null
    }

    val esActivo: Boolean get() = this == RECIBIDO || this == EN_PROCESO || this == LISTO

    companion object {
        fun desde(valor: String): EstadoPedido = valueOf(valor)
    }
}