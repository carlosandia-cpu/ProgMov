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

    private val _carga =
        MutableStateFlow<CargaUi>(CargaUi.Inicial)

    val carga: StateFlow<CargaUi> =
        _carga.asStateFlow()

    private val _tiempoSecuencial = MutableStateFlow<Long?>(null)
    val tiempoSecuencial: StateFlow<Long?> = _tiempoSecuencial.asStateFlow()

    private val _tiempoParalelo = MutableStateFlow<Long?>(null)
    val tiempoParalelo: StateFlow<Long?> = _tiempoParalelo.asStateFlow()

    private val _simularError = MutableStateFlow(false)

    val simularError: StateFlow<Boolean> =
        _simularError.asStateFlow()

    fun cambiarSimularError(valor: Boolean) {
        if (_carga.value is CargaUi.Cargando) return

        _simularError.value = valor
    }

    fun cargarUnaPorUna() {
        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {
            val inicio = SystemClock.elapsedRealtime()

            try {
                val perfil = repo.cargarPerfil()
                val pedidos = repo.cargarPedidos(
                    _simularError.value
                )

                val tiempo =
                    SystemClock.elapsedRealtime() - inicio

                _tiempoSecuencial.value = tiempo

                _carga.value = CargaUi.Exito(
                    modo = "Una por una",
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = tiempo
                )

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: " +
                            (e.message ?: "Error desconocido")
                )
            }
        }
    }

    fun cargarALaVez() {
        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {
            val inicio = SystemClock.elapsedRealtime()

            try {
                val (perfil, pedidos) = coroutineScope {
                    val perfilDeferred = async {
                        repo.cargarPerfil()
                    }

                    val pedidosDeferred = async {
                        repo.cargarPedidos(
                            _simularError.value
                        )
                    }

                    Pair(
                        perfilDeferred.await(),
                        pedidosDeferred.await()
                    )
                }

                val tiempo =
                    SystemClock.elapsedRealtime() - inicio

                _tiempoParalelo.value = tiempo

                _carga.value = CargaUi.Exito(
                    modo = "A la vez",
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = tiempo
                )

            } catch (e: Exception) {
                if (e is CancellationException) throw e

                _carga.value = CargaUi.Error(
                    "No se pudo cargar: " +
                            (e.message ?: "Error desconocido")
                )
            }
        }
    }
}
