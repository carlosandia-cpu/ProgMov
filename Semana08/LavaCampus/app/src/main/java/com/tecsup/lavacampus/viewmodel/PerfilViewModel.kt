package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Unidad
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PerfilViewModel(repo: LavaRepository, usuarioId: Int) : ViewModel() {

    private val pedidos = repo.observarPedidosDeUsuario(usuarioId)

    val activos: StateFlow<Int> = pedidos
        .map { lista -> lista.count { EstadoPedido.desde(it.pedido.estado).esActivo } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totales: StateFlow<Int> = pedidos
        .map { it.size }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    /** Kilos de los pedidos ya entregados. */
    val kilosLavados: StateFlow<Double> = pedidos
        .map { lista ->
            lista.filter { it.pedido.estado == EstadoPedido.ENTREGADO.name }
                .flatMap { it.detalles }
                .filter { it.servicio.unidad == Unidad.KG.name }
                .sumOf { it.detalle.cantidad }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)
}