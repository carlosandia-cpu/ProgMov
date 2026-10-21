package com.tecsup.lavacampus.data.local

import com.tecsup.lavacampus.data.local.entity.DetallePedido
import com.tecsup.lavacampus.data.local.entity.Pedido
import com.tecsup.lavacampus.data.local.entity.Servicio
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.model.EstadoPedido
import com.tecsup.lavacampus.model.Rol
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DatosIniciales {

    suspend fun sembrar(db: LavaDatabase) {
        if (db.usuarioDao().contar() != 0) return

        // 1. Servicios (ids 1 a 4 en una base nueva)
        val servicios = listOf(
            Servicio(nombre = "Lavado y secado", descripcion = "Lavado y secado por kilo",
                unidad = "KG", precio = 8.0, horasEntrega = 24, activo = true, icono = "wash"),
            Servicio(nombre = "Planchado", descripcion = "Planchado por prenda",
                unidad = "PRENDA", precio = 2.5, horasEntrega = 48, activo = true, icono = "iron"),
            Servicio(nombre = "Lavado en seco", descripcion = "Lavado en seco por prenda",
                unidad = "PRENDA", precio = 12.0, horasEntrega = 48, activo = true, icono = "shirt"),
            Servicio(nombre = "Edredón / frazada", descripcion = "Lavado de edredón o frazada",
                unidad = "UNIDAD", precio = 20.0, horasEntrega = 48, activo = false, icono = "wash")
        )
        db.servicioDao().insertarTodos(servicios)
        val precios = mapOf(1 to 8.0, 2 to 2.5, 3 to 12.0, 4 to 20.0)

        // 2. Usuarios (ids 1 a 4 en una base nueva)
        db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Ana Torres", codigo = "U202310234", correo = "ana@correo.test",
                    clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Luis Ramos", codigo = "U202310567", correo = "luis@correo.test",
                    clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Marta Quispe", codigo = "U202310891", correo = "marta@correo.test",
                    clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Administrador", codigo = "", correo = "admin@lavacampus.test",
                    clave = "admin", rol = Rol.ADMIN.name)
            )
        )
        val ana = 1
        val luis = 2
        val marta = 3

        // 3. Pedidos con sus líneas (el total sale de la suma de los detalles)
        val hoy = LocalDate.now()
        val diasDesdeLunes = ChronoUnit.DAYS.between(hoy.with(DayOfWeek.MONDAY), hoy).toInt()

        // Nunca retrocede más allá del lunes, para que "esta semana" siempre tenga ingresos
        fun hace(n: Int): LocalDate = hoy.minusDays(minOf(n, diasDesdeLunes).toLong())

        fun entrega(registro: LocalDate, dias: Long): LocalDate {
            var f = registro.plusDays(dias)
            if (f.dayOfWeek == DayOfWeek.SUNDAY) f = f.plusDays(1)
            return f
        }

        suspend fun crear(
            usuarioId: Int,
            registro: LocalDate,
            dias: Long,
            estado: EstadoPedido,
            casillero: String?,
            pagado: Boolean,
            lineas: List<Pair<Int, Double>>
        ) {
            val total = lineas.sumOf { (sid, cant) -> cant * precios.getValue(sid) }
            val pedidoId = db.pedidoDao().insertar(
                Pedido(
                    usuarioId = usuarioId,
                    fechaRegistro = registro.toString(),
                    fechaEntrega = entrega(registro, dias).toString(),
                    estado = estado.name,
                    total = total,
                    casillero = casillero,
                    pagado = pagado
                )
            ).toInt()
            db.detalleDao().insertarTodos(
                lineas.map { (sid, cant) ->
                    DetallePedido(
                        pedidoId = pedidoId,
                        servicioId = sid,
                        cantidad = cant,
                        precioUnitario = precios.getValue(sid),
                        subtotal = cant * precios.getValue(sid)
                    )
                }
            )
        }

        // ENTREGADOS y pagados (cuentan en los ingresos de la semana)
        crear(ana, hace(3), 2, EstadoPedido.ENTREGADO, null, true, listOf(1 to 4.0, 2 to 6.0))
        crear(luis, hace(2), 1, EstadoPedido.ENTREGADO, null, true, listOf(1 to 5.0))
        crear(marta, hace(1), 2, EstadoPedido.ENTREGADO, null, true, listOf(3 to 2.0))
        crear(ana, hace(2), 1, EstadoPedido.ENTREGADO, null, true, listOf(1 to 3.0))

        // LISTOS con casillero
        crear(luis, hace(1), 1, EstadoPedido.LISTO, "C-05", false, listOf(1 to 6.0, 2 to 4.0))
        crear(marta, hace(1), 2, EstadoPedido.LISTO, "C-07", false, listOf(3 to 1.0, 2 to 3.0))

        // EN PROCESO
        crear(ana, hoy, 1, EstadoPedido.EN_PROCESO, null, false, listOf(1 to 2.5))
        crear(luis, hace(1), 2, EstadoPedido.EN_PROCESO, null, false, listOf(2 to 8.0))

        // RECIBIDOS de hoy
        crear(marta, hoy, 2, EstadoPedido.RECIBIDO, null, false, listOf(1 to 3.5, 2 to 5.0))
        crear(ana, hoy, 2, EstadoPedido.RECIBIDO, null, false, listOf(2 to 4.0))
    }
}