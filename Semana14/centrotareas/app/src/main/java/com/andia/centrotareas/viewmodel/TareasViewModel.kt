package com.andia.centrotareas.viewmodel

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andia.centrotareas.data.TareasRepository
import com.andia.centrotareas.model.CargaUi
import com.andia.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class TareasViewModel(
    private val repo: TareasRepository
) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private var trabajo: Job? = null

    fun tareaLarga() {
        if (_corriendo.value) return

        _corriendo.value = true
        _progreso.value = 0f
        _mensaje.value = "Trabajando..."

        trabajo = viewModelScope.launch {
            try {
                Log.d(
                    "HILO",
                    "Antes: ${Thread.currentThread().name}"
                )

                withContext(Dispatchers.Default) {
                    Log.d(
                        "HILO",
                        "Dentro: ${Thread.currentThread().name}"
                    )

                    for (i in 1..100) {
                        delay(40)
                        _progreso.value = i / 100f
                    }
                }

                Log.d(
                    "HILO",
                    "Despues: ${Thread.currentThread().name}"
                )

                _mensaje.value = "Terminó"

            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                throw e

            } finally {
                _corriendo.value = false
                trabajo = null
            }
        }
    }

    fun cancelar() {
        if (!_corriendo.value) return

        _mensaje.value = "Cancelado"
        trabajo?.cancel()
    }

    private val _primos =
        MutableStateFlow<PrimosUi>(PrimosUi.Inicial)

    val primos: StateFlow<PrimosUi> =
        _primos.asStateFlow()

    private var trabajoPrimos: Job? = null
    private var generacionPrimos = 0L

    fun contar(hasta: Int) {
        if (
            hasta < 2 ||
            _primos.value is PrimosUi.Calculando
        ) {
            return
        }

        generacionPrimos++
        val generacionActual = generacionPrimos

        _primos.value = PrimosUi.Calculando(0f)

        trabajoPrimos = viewModelScope.launch {
            val inicio = SystemClock.elapsedRealtime()

            try {
                val cantidad = repo.contarPrimos(hasta) {
                        nuevoProgreso ->

                    withContext(Dispatchers.Main.immediate) {
                        if (
                            generacionActual == generacionPrimos &&
                            _primos.value is PrimosUi.Calculando
                        ) {
                            _primos.value = PrimosUi.Calculando(
                                nuevoProgreso
                            )
                        }
                    }
                }

                val tiempo =
                    SystemClock.elapsedRealtime() - inicio

                if (generacionActual == generacionPrimos) {
                    _primos.value = PrimosUi.Listo(
                        cantidad = cantidad,
                        milisegundos = tiempo
                    )
                }

            } catch (e: CancellationException) {
                if (generacionActual == generacionPrimos) {
                    val actual =
                        _primos.value as? PrimosUi.Calculando

                    _primos.value = PrimosUi.Cancelado(
                        actual?.progreso ?: 0f
                    )
                }

                throw e

            } finally {
                if (generacionActual == generacionPrimos) {
                    trabajoPrimos = null
                }
            }
        }
    }

    fun cancelarPrimos() {
        val actual =
            _primos.value as? PrimosUi.Calculando ?: return

        generacionPrimos++

        _primos.value = PrimosUi.Cancelado(
            progreso = actual.progreso
        )

        trabajoPrimos?.cancel()
        trabajoPrimos = null
    }

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _tiempoSecuencial = MutableStateFlow<Long?>(null)
    val tiempoSecuencial: StateFlow<Long?> = _tiempoSecuencial.asStateFlow()

    private val _tiempoParalelo = MutableStateFlow<Long?>(null)
    val tiempoParalelo: StateFlow<Long?> = _tiempoParalelo.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    private enum class ModoCarga { SECUENCIAL, PARALELA }

    private class IntentosAgotadosException(error: IOException) :
        IOException(error.message ?: "Error de conexión", error)

    private var ultimoModo: ModoCarga? = null

    fun cambiarSimularError(valor: Boolean) {
        if (_carga.value is CargaUi.Cargando) return
        _simularError.value = valor
    }

    private suspend fun <T> ejecutarConReintentos(
        bloque: suspend () -> T
    ): T {
        for (intento in 1..3) {
            _carga.value = CargaUi.Cargando(intento = intento)

            try {
                return bloque()
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                if (intento == 3) {
                    throw IntentosAgotadosException(e)
                }

                val esperaSegundos = intento.toLong()
                val unidad = if (esperaSegundos == 1L) "segundo" else "segundos"
                _carga.value = CargaUi.Cargando(
                    intento = intento,
                    mensajeEspera = "Reintentando en $esperaSegundos $unidad..."
                )
                delay(esperaSegundos * 1000L)
            }
        }

        error("Se agotaron los intentos")
    }

    fun cargarUnaPorUna() {
        iniciarCarga(ModoCarga.SECUENCIAL)
    }

    fun cargarALaVez() {
        iniciarCarga(ModoCarga.PARALELA)
    }

    private fun iniciarCarga(modo: ModoCarga) {
        if (_carga.value is CargaUi.Cargando) return

        ultimoModo = modo
        val simularError = _simularError.value
        _carga.value = CargaUi.Cargando(intento = 1)

        viewModelScope.launch {
            val inicio = SystemClock.elapsedRealtime()

            try {
                val (perfil, pedidos) = ejecutarConReintentos {
                    when (modo) {
                        ModoCarga.SECUENCIAL -> {
                            val perfil = repo.cargarPerfil()
                            val pedidos = repo.cargarPedidos(simularError)
                            Pair(perfil, pedidos)
                        }

                        ModoCarga.PARALELA -> coroutineScope {
                            val perfilPendiente = async { repo.cargarPerfil() }
                            val pedidosPendientes = async {
                                repo.cargarPedidos(simularError)
                            }
                            Pair(
                                perfilPendiente.await(),
                                pedidosPendientes.await()
                            )
                        }
                    }
                }

                val tiempo = SystemClock.elapsedRealtime() - inicio
                val nombreModo = when (modo) {
                    ModoCarga.SECUENCIAL -> {
                        _tiempoSecuencial.value = tiempo
                        "Una por una"
                    }
                    ModoCarga.PARALELA -> {
                        _tiempoParalelo.value = tiempo
                        "A la vez"
                    }
                }

                _carga.value = CargaUi.Exito(
                    modo = nombreModo,
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = tiempo
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val detalle = e.message ?: "Error desconocido"
                val mensaje = if (e is IntentosAgotadosException) {
                    "No se pudo completar la carga después de 3 intentos: $detalle"
                } else {
                    "No se pudo cargar: $detalle"
                }
                _carga.value = CargaUi.Error(mensaje)
            }
        }
    }

    fun reintentar() {
        if (_carga.value !is CargaUi.Error) return
        ultimoModo?.let { iniciarCarga(it) }
    }
}
