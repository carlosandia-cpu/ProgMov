package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.model.Comentario
import com.tecsup.murocampus.model.NuevoPost
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetalleUi(
    val post: Post,
    val comentarios: List<Comentario>
)

class PostsViewModel(
    private val repo: PostRepository
) : ViewModel() {

    private val _lista =
        MutableStateFlow<UiState<List<Post>>>(UiState.Cargando)

    val lista = _lista.asStateFlow()

    private val _detalle =
        MutableStateFlow<UiState<DetalleUi>>(UiState.Cargando)

    val detalle = _detalle.asStateFlow()

    private val _usuarioFiltro = MutableStateFlow<Int?>(null)

    val usuarioFiltro = _usuarioFiltro.asStateFlow()

    private val _creacion = MutableStateFlow<UiState<Post>?>(null)

    val creacion = _creacion.asStateFlow()

    private var detalleJob: Job? = null
    private var listaJob: Job? = null
    private var crearJob: Job? = null

    init {
        cargar()
    }

    fun cargar() {

        listaJob?.cancel()

        listaJob = viewModelScope.launch {

            _lista.value = UiState.Cargando

            val usuario = _usuarioFiltro.value

            val resultado = if (usuario == null) {
                repo.listar()
            } else {
                repo.porUsuario(usuario)
            }

            resultado
                .onSuccess { posts ->
                    _lista.value = UiState.Exito(posts)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun filtrar(userId: Int?) {
        _usuarioFiltro.value = userId
        cargar()
    }

    fun abrir(id: Int) {

        detalleJob?.cancel()

        detalleJob = viewModelScope.launch {

            _detalle.value = UiState.Cargando

            val post = repo.detalle(id)

            if (post.isFailure) {
                _detalle.value = UiState.Error(
                    mensajeDe(post.exceptionOrNull()!!)
                )
                return@launch
            }

            val comentarios = repo.comentarios(id)

            if (comentarios.isFailure) {
                _detalle.value = UiState.Error(
                    mensajeDe(comentarios.exceptionOrNull()!!)
                )
                return@launch
            }

            _detalle.value = UiState.Exito(
                DetalleUi(
                    post = post.getOrThrow(),
                    comentarios = comentarios.getOrThrow()
                )
            )
        }
    }

    fun cerrarDetalle() {
        detalleJob?.cancel()
    }

    fun publicar(titulo: String, texto: String) {

        if (titulo.isBlank() || texto.isBlank()) return

        if (_creacion.value == UiState.Cargando) return

        crearJob = viewModelScope.launch {

            _creacion.value = UiState.Cargando

            val nuevo = NuevoPost(
                userId = 1,
                title = titulo.trim(),
                body = texto.trim()
            )

            repo.crear(nuevo)
                .onSuccess { post ->
                    _creacion.value = UiState.Exito(post)
                }
                .onFailure { error ->
                    _creacion.value = UiState.Error(mensajeDe(error))
                }
        }
    }

    fun limpiarCreacion() {
        crearJob?.cancel()
        _creacion.value = null
    }
}