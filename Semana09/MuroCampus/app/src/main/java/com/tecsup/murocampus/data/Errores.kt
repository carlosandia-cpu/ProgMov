package com.tecsup.murocampus.data

import java.io.IOException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException

fun mensajeDe(e: Throwable): String = when (e) {

    is SerializationException ->
        "Respuesta inesperada"

    is HttpException ->
        "Error del servidor (${e.code()})"

    is IOException ->
        "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."

    else ->
        "Error desconocido"
}