
package com.andia.campusgps.data

import com.andia.campusgps.model.Posicion

interface UbicacionRepository {

    suspend fun obtenerPosicion(): Result<Posicion>

    fun playServicesDisponible(): Boolean
}
