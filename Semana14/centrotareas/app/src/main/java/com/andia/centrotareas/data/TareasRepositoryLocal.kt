
package com.andia.centrotareas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(
        simularError: Boolean
    ): List<String> {
        delay(3000)

        if (simularError) {
            throw IOException("Sin conexión")
        }

        return listOf(
            "Pedido 101",
            "Pedido 102",
            "Pedido 103"
        )
    }

    override suspend fun contarPrimos(
        hasta: Int,
        onProgress: suspend (Float) -> Unit
    ): Int = withContext(Dispatchers.Default) {

        if (hasta < 2) {
            onProgress(1f)
            return@withContext 0
        }

        val contexto = currentCoroutineContext()
        val total = hasta - 1
        val intervalo = maxOf(1, (total / 100) + 1)

        var cantidad = 0
        var siguienteReporte = intervalo

        for (n in 2..hasta) {
            contexto.ensureActive()

            if (esPrimo(n)) {
                cantidad++
            }

            val procesados = n - 1

            if (
                procesados >= siguienteReporte ||
                procesados == total
            ) {
                onProgress(
                    (procesados.toFloat() / total)
                        .coerceIn(0f, 1f)
                )

                siguienteReporte += intervalo
            }
        }

        cantidad
    }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false

        var divisor = 2

        while (divisor.toLong() * divisor <= n) {
            if (n % divisor == 0) {
                return false
            }

            divisor++
        }

        return true
    }
}
