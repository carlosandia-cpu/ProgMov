
package com.andia.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import com.andia.campusgps.model.Posicion
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(
    private val context: Context
) : UbicacionRepository {

    private val cliente =
        LocationServices.getFusedLocationProviderClient(context)

    override fun playServicesDisponible(): Boolean {
        return GoogleApiAvailability.getInstance()
            .isGooglePlayServicesAvailable(context) ==
                ConnectionResult.SUCCESS
    }

    private fun ubicacionActivada(): Boolean {
        val manager = context.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        return try {
            manager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            ) || manager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )
        } catch (e: Exception) {
            false
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun obtenerPosicion(): Result<Posicion> {
        return try {

            if (!ubicacionActivada()) {
                return Result.failure(
                    IllegalStateException(
                        "Activa el GPS e intenta de nuevo"
                    )
                )
            }

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
                        "Activa el GPS e intenta de nuevo"
                    )
                )
            }

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
