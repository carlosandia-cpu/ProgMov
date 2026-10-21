package com.tecsup.lavacampus.data.repository

import androidx.room.withTransaction
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.data.local.relation.PedidoConDetalle
import com.tecsup.lavacampus.model.ClienteResumen
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.IngresoServicio
import com.tecsup.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class LavaRepositoryRoom(private val db: LavaDatabase) : LavaRepository {

    private val servicioDao = db.servicioDao()
    private val usuarioDao = db.usuarioDao()
    private val pedidoDao = db.pedidoDao()
    private val detalleDao = db.detalleDao()

    // Usuarios
    override suspend fun login(correo: String, clave: String): Usuario? =
        usuarioDao.login(correo, clave)

    override suspend fun obtenerUsuario(id: Int): Usuario? = usuarioDao.obtener(id)

    override suspend fun primerUsuarioDeRol(rol: String): Usuario? = usuarioDao.primeroDeRol(rol)

    override fun observarClientes(): Flow<List<ClienteResumen>> =
        usuarioDao.observarClientes()

    // Servicios
    override fun observarServicios(): Flow<List<Servicio>> = servicioDao.observarTodos()

    override fun observarServiciosActivos(): Flow<List<Servicio>> = servicioDao.observarActivos()

    override suspend fun guardarServicio(s: Servicio) {
        if (s.id == 0) servicioDao.insertar(s) else servicioDao.actualizar(s)
    }

    override suspend fun eliminarServicio(s: Servicio) = servicioDao.eliminar(s)

    override suspend fun cambiarActivo(id: Int, activo: Boolean) =
        servicioDao.cambiarActivo(id, activo)

    override suspend fun servicioTieneDetalles(id: Int): Boolean =
        detalleDao.contarPorServicio(id) > 0

    // Pedidos
    override suspend fun crearPedido(
        usuarioId: Int,
        lineas: List<LineaPedido>,
        fechaEntrega: String,
        nota: String
    ): Long = db.withTransaction {
        // Congela el precio de cada servicio en el momento del pedido
        val detallesBase = lineas.map { linea ->
            val servicio = servicioDao.obtener(linea.servicioId)
                ?: throw IllegalStateException("Servicio ${linea.servicioId} no existe")
            val subtotal = linea.cantidad * servicio.precio
            Triple(linea, servicio.precio, subtotal)
        }
        val total = detallesBase.sumOf { it.third }

        val pedidoId = pedidoDao.insertar(
            Pedido(
                usuarioId = usuarioId,
                fechaRegistro = LocalDate.now().toString(),
                fechaEntrega = fechaEntrega,
                estado = EstadoPedido.RECIBIDO.name,
                total = total,
                casillero = null,
                pagado = false,
                nota = nota
            )
        )

        detalleDao.insertarTodos(
            detallesBase.map { (linea, precio, subtotal) ->
                DetallePedido(
                    pedidoId = pedidoId.toInt(),
                    servicioId = linea.servicioId,
                    cantidad = linea.cantidad,
                    precioUnitario = precio,
                    subtotal = subtotal
                )
            }
        )
        pedidoId
    }

    override suspend fun contarActivos(usuarioId: Int): Int = pedidoDao.contarActivos(usuarioId)

    override fun observarPedidosDeUsuario(usuarioId: Int): Flow<List<PedidoConDetalle>> =
        pedidoDao.observarPorUsuario(usuarioId)

    override fun observarPedidos(): Flow<List<PedidoConDetalle>> = pedidoDao.observarTodos()

    override fun observarPedido(id: Int): Flow<PedidoConDetalle?> = pedidoDao.observarUno(id)

    // No valida reglas: las reglas viven en el ViewModel
    override suspend fun cambiarEstado(id: Int, estado: EstadoPedido, casillero: String?) =
        pedidoDao.actualizarEstado(id, estado.name, casillero)

    override suspend fun marcarPagado(id: Int, pagado: Boolean) =
        pedidoDao.actualizarPagado(id, pagado)

    override fun observarCasillerosOcupados(): Flow<List<String>> =
        pedidoDao.observarCasillerosOcupados()

    // Panel
    override fun observarPedidosDeFecha(fecha: String): Flow<Int> = pedidoDao.contarPorFecha(fecha)

    override fun observarCantidadPorEstado(estado: EstadoPedido): Flow<Int> =
        pedidoDao.contarPorEstado(estado.name)

    override fun observarIngresosDesde(fechaInicio: String): Flow<Double> =
        pedidoDao.ingresosDesde(fechaInicio)

    override fun observarIngresosPorServicio(fechaInicio: String): Flow<List<IngresoServicio>> =
        detalleDao.observarIngresosPorServicio(fechaInicio)
}