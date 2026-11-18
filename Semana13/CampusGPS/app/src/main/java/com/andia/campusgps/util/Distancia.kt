package com.andia.campusgps.util

import android.location.Location
import com.andia.campusgps.model.Lugar
import com.andia.campusgps.model.Posicion

/** Distancia en metros entre la posición del usuario y un lugar. */
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud, desde.longitud,
        hasta.latitud, hasta.longitud,
        resultado
    )
    return resultado[0]
}