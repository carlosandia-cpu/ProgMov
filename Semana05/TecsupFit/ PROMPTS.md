# PROMPTS.md — Fase 2: Mejora con IA (rama `mejora-ia`)

## Mejora implementada
Cancelar una reserva desde la pantalla **Mis reservas**, con un `AlertDialog` de
confirmación antes de eliminarla (tal como sugiere la guía de la actividad).

## Prompt 1
**Le pedí:**
"Tengo una app Compose (TECSUP Fit) con una pantalla `ReservationsScreen` que
muestra una `LazyColumn` de `Reserva` (estado se guarda en `AppNavigation` con
`mutableStateListOf`). Quiero agregar la posibilidad de cancelar una reserva,
mostrando un `AlertDialog` de confirmación antes de eliminarla. No debo usar
ViewModel, solo `remember`/`mutableStateOf`, igual que el resto del proyecto."

**Qué generó:** Un botón "Cancelar" en cada `ReservaCard` y un `AlertDialog`
controlado por una variable de estado que guarda la reserva seleccionada,
más un callback `onCancelarReserva` para que `AppNavigation` remueva el
elemento de la lista.

**Qué tuve que corregir:**
- El código generado importaba `androidx.compose.material.icons.filled.Delete`
  para el botón, pero el proyecto no usa `material-icons-extended` (el
  `BottomNavigationBar` usa símbolos de texto, no íconos). Cambié el botón a
  `OutlinedButton` con el texto "Cancelar" para no agregar una dependencia
  nueva y mantener el mismo estilo visual del resto de la app.
- Dejé importado por error `Button` sin usar; lo quité para evitar el warning
  de import no usado.

## Prompt 2
**Le pedí:**
"Que el botón de cancelar solo aparezca si la reserva sigue en estado
'Confirmada', y no en reservas ya 'Completada', para no permitir cancelar
algo que ya pasó."

**Qué generó:** Envolvió el `OutlinedButton` en un `if (reserva.estado ==
"Confirmada")` dentro de la `Row` que ya mostraba el estado.

**Qué tuve que corregir:** Nada relevante; solo ajusté el `Arrangement` de la
`Row` a `SpaceBetween` para que el estado quede a la izquierda y el botón a
la derecha, en vez de quedar los dos pegados como los propuso la IA.

## Prompt 3
**Le pedí:**
"Muéstrame cómo conectar el callback `onCancelarReserva` en `AppNavigation.kt`
para remover la reserva de la lista mutable que ya tengo."

**Qué generó:** `onCancelarReserva = { reserva -> reservas.remove(reserva) }`
dentro del `composable("reservas")`. Lo usé tal cual, sin cambios, porque
`reservas` ya es un `mutableStateListOf<Reserva>` y `Reserva` es un
`data class`, así que `remove` funciona por igualdad de valores sin lógica
adicional.