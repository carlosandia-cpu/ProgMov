package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.Casilleros
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repo: LavaRepository,
    private val pedidoId: Int
) : ViewModel() {

    val pedido: StateFlow<PedidoConDetalle?> = repo.observarPedido(pedidoId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    /** El casillero actual del propio pedido no cuenta como ocupado. */
    val casillerosOcupados: StateFlow<List<String>> = combine(
        repo.observarCasillerosOcupados(), pedido
    ) { ocupados, p ->
        ocupados.filter { it != p?.pedido?.casillero }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _casilleroElegido = MutableStateFlow<String?>(null)
    val casilleroElegido: StateFlow<String?> = _casilleroElegido.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    fun elegirCasillero(c: String) {
        if (c in casillerosOcupados.value) {
            _mensaje.value = "El casillero $c ya está ocupado"
            return
        }
        _casilleroElegido.value = c
    }

    /** Solo al estado siguiente. LISTO exige casillero libre; ENTREGADO exige pago. */
    fun avanzar() {
        viewModelScope.launch {
            val actual = repo.observarPedido(pedidoId).first() ?: return@launch
            val estado = EstadoPedido.desde(actual.pedido.estado)
            val siguiente = estado.siguiente()
            if (siguiente == null) {
                _mensaje.value = "Este pedido ya está finalizado"
                return@launch
            }

            var casillero = actual.pedido.casillero
            when (siguiente) {
                EstadoPedido.EN_PROCESO -> casillero = null
                EstadoPedido.LISTO -> {
                    val elegido = _casilleroElegido.value
                    if (elegido == null) {
                        _mensaje.value = "Elige un casillero antes de marcar el pedido como listo"
                        return@launch
                    }
                    if (!Casilleros.esValido(elegido)) {
                        _mensaje.value = "El casillero debe ser de C-01 a C-30"
                        return@launch
                    }
                    if (elegido in repo.observarCasillerosOcupados().first()) {
                        _mensaje.value = "El casillero $elegido ya está ocupado"
                        return@launch
                    }
                    casillero = elegido
                }
                EstadoPedido.ENTREGADO -> {
                    if (!actual.pedido.pagado) {
                        _mensaje.value = "Marca el pedido como Cobrado antes de entregarlo"
                        return@launch
                    }
                }
                else -> Unit
            }
            repo.cambiarEstado(pedidoId, siguiente, casillero)
        }
    }

    /** Solo desde RECIBIDO o EN_PROCESO. */
    fun cancelar() {
        viewModelScope.launch {
            val actual = repo.observarPedido(pedidoId).first() ?: return@launch
            val estado = EstadoPedido.desde(actual.pedido.estado)
            if (estado == EstadoPedido.RECIBIDO || estado == EstadoPedido.EN_PROCESO) {
                repo.cambiarEstado(pedidoId, EstadoPedido.CANCELADO, null)
            } else {
                _mensaje.value = "Solo se cancelan pedidos Recibidos o En proceso"
            }
        }
    }

    fun marcarPagado(valor: Boolean) {
        val estado = pedido.value?.pedido?.estado?.let { EstadoPedido.desde(it) } ?: return
        if (!estado.esActivo) return
        viewModelScope.launch { repo.marcarPagado(pedidoId, valor) }
    }
}