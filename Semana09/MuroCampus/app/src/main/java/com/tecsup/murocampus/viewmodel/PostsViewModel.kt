package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.murocampus.data.PostRepository
import com.tecsup.murocampus.model.Post
import com.tecsup.murocampus.model.Comentario
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

    private var detalleJob: Job? = null

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {

            _lista.value = UiState.Cargando

            repo.listar()
                .onSuccess { posts ->
                    _lista.value = UiState.Exito(posts)
                }
                .onFailure { error ->
                    _lista.value = UiState.Error(mensajeDe(error))
                }
        }
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
}