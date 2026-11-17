
package com.andia.campusgps.data

import com.andia.campusgps.model.Lugar

class LugaresRepositoryLocal : LugaresRepository {

    override fun obtenerLugares(): List<Lugar> {
        return listOf(
            Lugar(
                id = 1,
                nombre = "Biblioteca Central",
                categoria = "Estudio",
                latitud = -12.0460,
                longitud = -77.0435
            ),
            Lugar(
                id = 2,
                nombre = "Cafetería Campus",
                categoria = "Comida",
                latitud = -12.0470,
                longitud = -77.0420
            ),
            Lugar(
                id = 3,
                nombre = "Laboratorio de Cómputo",
                categoria = "Estudio",
                latitud = -12.0455,
                longitud = -77.0415
            ),
            Lugar(
                id = 4,
                nombre = "Auditorio Principal",
                categoria = "Eventos",
                latitud = -12.0480,
                longitud = -77.0440
            ),
            Lugar(
                id = 5,
                nombre = "Estacionamiento Norte",
                categoria = "Servicios",
                latitud = -12.0440,
                longitud = -77.0430
            )
        )
    }
}
