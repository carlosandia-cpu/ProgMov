# Prompts utilizados

Fase 2 de LavaCampus (rama `mejora-ia`). Herramienta: Gemini en Android Studio.
Cada prompt se envió adjuntando los archivos relevantes del proyecto y pidiendo respetar MVVM + Repository.

## Prompt 1 · Pedidos atrasados en la bandeja

**Texto enviado a Gemini:**
Tengo una app Kotlin con Jetpack Compose, Material 3, MVVM, Room y un LavaRepository (interface). Agrega en la bandeja del administrador una marca roja para los pedidos atrasados: fechaEntrega (String aaaa-MM-dd) menor que hoy y estado distinto de ENTREGADO y CANCELADO. La consulta va en PedidoDao, la funcion se declara primero en la interface LavaRepository y luego en LavaRepositoryRoom, y el valor se expone como StateFlow en BandejaViewModel (por ejemplo, el conjunto de ids atrasados). La View solo lo muestra: borde o texto "Atrasado" en rojo en la tarjeta de la bandeja. No uses XML ni librerias externas. Muestrame solo los archivos que cambian.

**Que genero la IA:**
- `PedidoDao.observarIdsAtrasados(hoy)` con la consulta `fechaEntrega < :hoy` y estado distinto de ENTREGADO y CANCELADO.
- La misma funcion declarada en `LavaRepository` e implementada en `LavaRepositoryRoom`.
- `BandejaViewModel.idsAtrasados: StateFlow<Set<Int>>` con `LocalDate.now()`.
- En `BandejaScreen`, borde rojo y el texto "Atrasado" en la tarjeta.

**Que tuve que corregir yo:**
- Faltaban imports (`LocalDate`, `map`, `BorderStroke`).
- Verifiqué que los pedidos sembrados con entrega del 19 de octubre aparecieran marcados (hoy es 21), porque sin pedidos vencidos no se ve el resultado.

**Respeta MVVM?:** Si. La consulta esta en el DAO, la funcion se declaro primero en la interface, el ViewModel solo conoce `LavaRepository` y la View solo muestra el estado.

## Prompt 2 · Descuento por volumen

**Texto enviado a Gemini:**
En mi app LavaCampus (Kotlin, Compose, Material 3, MVVM, Room, LavaRepository como interface) agrega un descuento del 10% cuando la cantidad de un servicio por kilo (unidad KG) supera 5 kg. El descuento se aplica sobre el subtotal de esa linea, se muestra en el resumen de NuevoPedidoScreen y se guarda en el pedido. Cambios: agrega a la entidad Pedido el campo descuento: Double = 0.0, sube la version de LavaDatabase a 2 y usa fallbackToDestructiveMigration. La regla se calcula en NuevoPedidoViewModel (estado descuento y total final). La funcion crearPedido de la interface LavaRepository recibe el descuento y LavaRepositoryRoom lo guarda, con total = suma de subtotales - descuento, dentro de la misma transaccion. La View no calcula nada, solo muestra. No rompas el limite de 3 pedidos activos ni el calculo de fecha de entrega. No uses XML ni librerias externas. Muestrame solo los archivos que cambian.

**Que genero la IA:**
- Campo `descuento` en la entidad `Pedido`.
- `LavaDatabase` en version 2 con `fallbackToDestructiveMigration(true)`.
- Nueva firma de `crearPedido` con el parametro `descuento` en la interface y en `LavaRepositoryRoom` (total = suma de subtotales - descuento, dentro de `withTransaction`).
- En `NuevoPedidoViewModel`, el descuento por linea (10 % si es KG y supera 5 kg), y los estados `descuento` y `total`.
- En `NuevoPedidoScreen`, una fila de descuento en el resumen.

**Que tuve que corregir yo:**
- El descuento se mostraba con el color de error (rojo), que parece un fallo. Lo cambie al color primario del tema.
- El texto "Descuento (10%)" quedo escrito fijo en la View. No calcula nada, pero lo anoto como detalle menor.
- Limitacion: el `subtotal` de cada detalle no incluye el descuento, solo el `total` del pedido. Por eso "Ingresos por servicio" del panel puede sumar mas que "Ingresos de la semana" cuando hay un pedido con descuento pagado.
- Comprobe que 5.0 kg no aplica descuento y que 5.5 kg da S/ 39.60.

**Respeta MVVM?:** Si. La regla del descuento vive en el ViewModel, el Repository solo guarda el valor que recibe y la View solo muestra. La funcion se declaro primero en la interface y se subio la version de la base.

## Prompt 3 · Repetir un pedido

**Texto enviado a Gemini:**
En mi app LavaCampus (Kotlin, Compose, Material 3, MVVM, Room, LavaRepository como interface) agrega un boton "Pedir de nuevo" en los pedidos del Historial de MisPedidosScreen. Al pulsarlo debe abrir Nuevo pedido con las mismas lineas del pedido original, usando los precios actuales y solo los servicios activos (los inactivos se omiten), respetando los limites de cantidad de cada unidad. Pasa el id del pedido a Nuevo pedido como argumento opcional en la ruta (Rutas y NavLava). La logica de cargar las cantidades va en NuevoPedidoViewModel usando repo.observarPedido(id); la factory debe recibir el id. La View solo avisa el clic y muestra. No uses XML ni librerias externas. Muestrame solo los archivos que cambian.

**Que genero la IA:**
- `Rutas.NUEVO_PEDIDO` con el argumento opcional `pedidoId` y la funcion `Rutas.nuevoPedido(id)`.
- Ruta de `NavLava` con `navArgument` (valor por defecto 0) y llamada a la factory con el `pedidoId`.
- `LavaViewModelFactory` con el `pedidoId` para `NuevoPedidoViewModel`.
- Un bloque `init` en `NuevoPedidoViewModel` que carga las lineas con `repo.observarPedido(id)`, omite servicios inactivos y ajusta cantidades a los limites de la unidad.
- Boton "Pedir de nuevo" en `PedidoCard` y callback `onPedirDeNuevo` en `MisPedidosScreen`, solo en la pestana Historial.

**Que tuve que corregir yo:**
- Al cambiar `NUEVO_PEDIDO` a `"nuevo_pedido?pedidoId={pedidoId}"`, los botones "Nuevo pedido" de Inicio y el `+` de Mis pedidos podian seguir navegando con la constante vieja y cerrar la app. Los revise con una busqueda en el proyecto y deben usar `Rutas.nuevoPedido()`.
- El bloque `init` quedo antes de las propiedades que usa (`_cantidades`). Lo movi despues de ellas para evitar errores de inicializacion.
- Revise los imports de `PedidoCard` (`FilledTonalButton`).

**Respeta MVVM?:** Si. La carga de lineas esta en el ViewModel y usa solo la interface `LavaRepository`. La View solo avisa el clic y la navegacion queda en `NavLava`.

## Reflexion

- **Que mejoro la IA y que no?** Agilizo el codigo repetitivo (DAO, interface, implementacion, StateFlow y tarjetas) y respeto bien las capas MVVM en los tres cambios. No supo evaluar el impacto en el resto de la app: no actualizo las rutas viejas ni considero como afectaba el descuento a los ingresos del panel.
- **Que error cometio?** El mas serio fue cambiar la ruta de Nuevo pedido sin actualizar los lugares que ya la usaban, lo que cerraria la app. Tambien dejo un `init` antes de las propiedades que usaba, uso un color de error para un descuento y escribio el 10 % fijo en la View.
- **Que aprendi sobre MVVM, Room y reglas de negocio?** Que cada cambio debe empezar por la capa correcta: la funcion en la interface antes de implementarla, la regla en el ViewModel y la View solo para mostrar. Que cambiar una entidad obliga a subir la version de la base de datos, y que una regla de negocio nueva (como el descuento) puede afectar consultas que ya existian, por ejemplo las del panel.
- **Que aprendi sobre usar IA?** Hay que darle contexto con los archivos reales, pedirle poco a la vez y revisar siempre su resultado contra las reglas de arquitectura. Lo que genera compila, pero no siempre es seguro para el resto del proyecto.