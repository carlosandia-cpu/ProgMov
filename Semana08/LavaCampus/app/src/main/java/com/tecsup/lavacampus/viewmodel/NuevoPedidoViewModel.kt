package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.LineaPedido
import com.tecsup.lavacampus.model.Unidad
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate

data class LineaResumen(
    val servicio: Servicio,
    val cantidad: Double,
    val subtotal: Double,
    val descuento: Double = 0.0
)

class NuevoPedidoViewModel(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore,
    private val usuarioId: Int,
    private val pedidoId: Int = 0
) : ViewModel() {

    val servicios: StateFlow<List<Servicio>> = repo.observarServiciosActivos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recibirPedidos: StateFlow<Boolean> = prefs.recibirPedidos
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    private val _cantidades = MutableStateFlow<Map<Int, Double>>(emptyMap())
    val cantidades: StateFlow<Map<Int, Double>> = _cantidades.asStateFlow()

    private val _nota = MutableStateFlow("")
    val nota: StateFlow<String> = _nota.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private var enviando = false

    /** Una línea por cada servicio elegido (cantidad > 0). 10 % de descuento en KG si supera 5 kg. */
    val resumen: StateFlow<List<LineaResumen>> = combine(servicios, _cantidades) { lista, cant ->
        lista.mapNotNull { s ->
            val c = cant[s.id] ?: 0.0
            if (c > 0.0) {
                val subtotal = c * s.precio
                val descuentoLinea =
                    if (s.unidad == Unidad.KG.name && c > 5.0) subtotal * 0.10 else 0.0
                LineaResumen(s, c, subtotal, descuentoLinea)
            } else null
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val descuento: StateFlow<Double> = resumen
        .map { lineas -> lineas.sumOf { it.descuento } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val total: StateFlow<Double> = combine(resumen, descuento) { lineas, desc ->
        maxOf(0.0, lineas.sumOf { it.subtotal } - desc)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    /** Hoy + el mayor tiempo de entrega (24 h = 1 día). Si cae domingo, pasa al lunes. */
    val fechaEntrega: StateFlow<String?> = resumen.map { lineas ->
        if (lineas.isEmpty()) {
            null
        } else {
            val horas = lineas.maxOf { it.servicio.horasEntrega }
            val dias = ((horas + 23) / 24).toLong()
            var fecha = LocalDate.now().plusDays(dias)
            if (fecha.dayOfWeek == DayOfWeek.SUNDAY) fecha = fecha.plusDays(1)
            fecha.toString()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // "Pedir de nuevo": carga las líneas del pedido original (solo servicios activos,
    // con los límites de cada unidad). Los precios que se muestran son los actuales.
    init {
        if (pedidoId > 0) {
            viewModelScope.launch {
                val original = repo.observarPedido(pedidoId).first() ?: return@launch
                val mapa = mutableMapOf<Int, Double>()
                for (linea in original.detalles) {
                    val servicio = linea.servicio
                    if (servicio.activo) {
                        val unidad = Unidad.desde(servicio.unidad)
                        mapa[servicio.id] = linea.detalle.cantidad
                            .coerceIn(unidad.minimo, unidad.maximo)
                    }
                }
                _cantidades.value = mapa
            }
        }
    }

    /** KG: pasos de 0.5, mínimo 1.0, máximo 10.0. PRENDA/UNIDAD: pasos de 1, máximo 30. Cantidad 0 = apagado. */
    fun cambiarCantidad(servicioId: Int, delta: Double) {
        val servicio = servicios.value.firstOrNull { it.id == servicioId } ?: return
        val unidad = Unidad.desde(servicio.unidad)
        val actual = _cantidades.value[servicioId] ?: 0.0
        val nueva = when {
            actual == 0.0 && delta > 0 -> unidad.minimo
            actual + delta < unidad.minimo -> 0.0
            else -> minOf(actual + delta, unidad.maximo)
        }
        _cantidades.value = _cantidades.value + (servicioId to nueva)
    }

    fun actualizarNota(texto: String) {
        _nota.value = texto
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun confirmar(onCreado: () -> Unit) {
        val lineas = resumen.value
        val fecha = fechaEntrega.value
        if (lineas.isEmpty() || fecha == null || enviando) return
        enviando = true
        viewModelScope.launch {
            if (!prefs.recibirPedidos.first()) {
                _mensaje.value = "La lavandería no está recibiendo pedidos"
                enviando = false
                return@launch
            }
            if (repo.contarActivos(usuarioId) >= 3) {
                _mensaje.value = "Ya tienes 3 pedidos activos"
                enviando = false
                return@launch
            }
            repo.crearPedido(
                usuarioId = usuarioId,
                lineas = lineas.map { LineaPedido(it.servicio.id, it.cantidad) },
                fechaEntrega = fecha,
                descuento = descuento.value,
                nota = _nota.value.trim()
            )
            enviando = false
            onCreado()
        }
    }
}