package com.tecsup.lavacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.ui.components.graficos.DatoGrafico
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

class PanelViewModel(repo: LavaRepository) : ViewModel() {

    private val hoy = LocalDate.now().toString()
    private val lunes = LocalDate.now()
        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).toString()

    private val colores = listOf(
        Color(0xFF6D28D9), Color(0xFF0891B2), Color(0xFFD97706), Color(0xFF16A34A)
    )

    val pedidosHoy: StateFlow<Int> = repo.observarPedidosDeFecha(hoy)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val enProceso: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val porRecoger: StateFlow<Int> = repo.observarCantidadPorEstado(EstadoPedido.LISTO)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val ingresosSemana: StateFlow<Double> = repo.observarIngresosDesde(lunes)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val porEstado: StateFlow<List<DatoGrafico>> = combine(
        repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO),
        repo.observarCantidadPorEstado(EstadoPedido.EN_PROCESO),
        repo.observarCantidadPorEstado(EstadoPedido.LISTO),
        repo.observarCantidadPorEstado(EstadoPedido.ENTREGADO)
    ) { recibido, proceso, listo, entregado ->
        listOf(
            DatoGrafico(EstadoPedido.RECIBIDO.etiqueta, recibido.toDouble(), EstadoPedido.RECIBIDO.color),
            DatoGrafico(EstadoPedido.EN_PROCESO.etiqueta, proceso.toDouble(), EstadoPedido.EN_PROCESO.color),
            DatoGrafico(EstadoPedido.LISTO.etiqueta, listo.toDouble(), EstadoPedido.LISTO.color),
            DatoGrafico(EstadoPedido.ENTREGADO.etiqueta, entregado.toDouble(), EstadoPedido.ENTREGADO.color)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> = repo.observarIngresosPorServicio(lunes)
        .map { lista ->
            lista.mapIndexed { i, ingreso ->
                DatoGrafico(ingreso.nombre, ingreso.total, colores[i % colores.size])
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}