package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FiltroBandeja(val etiqueta: String, val estado: EstadoPedido?) {
    TODOS("Todos", null),
    RECIBIDO("Recibido", EstadoPedido.RECIBIDO),
    EN_PROCESO("En proceso", EstadoPedido.EN_PROCESO),
    LISTO("Listo", EstadoPedido.LISTO)
}

class BandejaViewModel(private val repo: LavaRepository) : ViewModel() {

    private val _filtro = MutableStateFlow(FiltroBandeja.TODOS)
    val filtro: StateFlow<FiltroBandeja> = _filtro.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    val pedidos: StateFlow<List<PedidoConDetalle>> = combine(
        repo.observarPedidos(), _filtro, _busqueda
    ) { lista, filtro, texto ->
        val estado = filtro.estado
        val consulta = texto.trim().lowercase()
        lista.filter { p ->
            (estado == null || p.pedido.estado == estado.name) &&
                    (consulta.isEmpty() ||
                            p.usuario.nombre.lowercase().contains(consulta) ||
                            p.pedido.id.toString().contains(consulta))
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun elegirFiltro(f: FiltroBandeja) {
        _filtro.value = f
    }

    fun buscar(texto: String) {
        _busqueda.value = texto
    }

    /** Pasa de RECIBIDO a EN_PROCESO. Los demás pasos (casillero, cobro) se hacen en la gestión. */
    fun iniciar(item: PedidoConDetalle) {
        if (item.pedido.estado != EstadoPedido.RECIBIDO.name) return
        viewModelScope.launch {
            repo.cambiarEstado(item.pedido.id, EstadoPedido.EN_PROCESO, null)
        }
    }
}