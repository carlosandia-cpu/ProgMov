package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MisPedidosViewModel(
    private val repo: LavaRepository,
    usuarioId: Int
) : ViewModel() {

    private val todos = repo.observarPedidosDeUsuario(usuarioId)

    /** RECIBIDO, EN_PROCESO y LISTO */
    val activos: StateFlow<List<PedidoConDetalle>> = todos
        .map { lista -> lista.filter { EstadoPedido.desde(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** ENTREGADO y CANCELADO */
    val historial: StateFlow<List<PedidoConDetalle>> = todos
        .map { lista -> lista.filter { !EstadoPedido.desde(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Misma regla del Paso 9: solo se cancela en RECIBIDO. */
    fun cancelar(item: PedidoConDetalle) {
        if (item.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(item.pedido.id, EstadoPedido.CANCELADO, null)
        }
    }
}