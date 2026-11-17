
package com.andia.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.andia.campusgps.data.LugaresRepository
import com.andia.campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            return MapaViewModel(
                ubicacionRepo,
                lugaresRepo
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido: ${modelClass.name}"
        )
    }
}
