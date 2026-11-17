
package com.andia.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import com.andia.campusgps.model.Posicion
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    context: Context
) : UbicacionRepository {

    private val cliente =
        LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> {
        return try {
            val cancelacion = CancellationTokenSource()

            val ubicacion = cliente.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancelacion.token
            ).await()

            if (ubicacion != null) {
                Result.success(
                    Posicion(
                        latitud = ubicacion.latitude,
                        longitud = ubicacion.longitude
                    )
                )
            } else {
                Result.failure(
                    IllegalStateException(
                        "No se pudo obtener la ubicación. Verifica que el GPS esté activado."
                    )
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
