package com.tecsup.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.data.repository.LavaRepository

class LavaViewModelFactory(
    private val repo: LavaRepository,
    private val prefs: PreferenciasStore,
    private val usuarioId: Int = 0,
    private val pedidoId: Int = 0
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(SesionViewModel::class.java) -> SesionViewModel(repo, prefs) as T
        modelClass.isAssignableFrom(InicioViewModel::class.java) -> InicioViewModel(repo, usuarioId) as T
        modelClass.isAssignableFrom(NuevoPedidoViewModel::class.java) -> NuevoPedidoViewModel(repo, usuarioId) as T
        modelClass.isAssignableFrom(DetallePedidoViewModel::class.java) -> DetallePedidoViewModel(repo, pedidoId) as T
        modelClass.isAssignableFrom(MisPedidosViewModel::class.java) -> MisPedidosViewModel(repo, usuarioId) as T
        modelClass.isAssignableFrom(TarifasViewModel::class.java) -> TarifasViewModel(repo) as T
        else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}