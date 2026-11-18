
package com.andia.campusgps.data

import com.andia.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
