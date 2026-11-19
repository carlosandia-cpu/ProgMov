# PROMPTS.md — CentroTareas, Laboratorio 14

**Curso:** Programación en Móviles  
**Aplicación:** CentroTareas  
**Rama de trabajo:** `mejora-ia`  
**Tecnologías:** Kotlin, Jetpack Compose, Material 3, corrutinas, StateFlow y MVVM + Repository  
**IA utilizada:** Gemini (propuestas de código), con revisión, corrección y pruebas posteriores.

## Registro de prompts y decisiones

La Fase 2 se realizó a partir de la aplicación funcional de `main`. Las tres mejoras se implementaron por separado para facilitar su revisión y seguimiento en Git. Esta tabla registra los prompts principales, la propuesta de Gemini, las correcciones realizadas y la justificación técnica.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **1. Primos con progreso.** «Necesito implementar una mejora funcional: mostrar el progreso real del cálculo de números primos. Mantener `Dispatchers.Default`, `StateFlow`, cancelación con `ensureActive()`, porcentaje al cancelar, reinicio en 0 % y resultado con tiempo. No modificar los otros módulos ni el diseño». | Propuso cambios en `Estados.kt`, `TareasRepository.kt`, `TareasRepositoryLocal.kt`, `TareasViewModel.kt` y `PantallaTareas.kt` para reportar el avance del cálculo y mostrarlo en una barra. | Se limitaron los reportes a aproximadamente cada 1 %; se mantuvo visible la barra al 100 % al completar; se ajustó el diseño para pantallas pequeñas y se protegió el estado frente a cálculos anteriores cancelados. | Informar por cada número procesado podía degradar el rendimiento; el porcentaje debía reflejar trabajo real y mantenerse correcto al finalizar o cancelar. |
| **2. Comparación de tiempos.** «Medir con `SystemClock.elapsedRealtime()` los tiempos reales de `Una por una` y `A la vez`, conservar los últimos resultados exitosos con `StateFlow`, dibujar dos barras proporcionales y calcular el ahorro y porcentaje; mantener errores, reintento, primos y diseño». | Propuso modificaciones en `TareasViewModel.kt` y `PantallaTareas.kt` para guardar por separado los tiempos y mostrar una comparación dentro de `Dos cargas`. | Se eliminó un mínimo visual artificial del 10 % en las barras; se agregó el porcentaje si el modo paralelo resulta más lento y se simplificaron las etiquetas para evitar desbordes. | Las barras y los mensajes deben reflejar los tiempos medidos, incluso cuando el paralelismo no supone una mejora. |
| **3. Reintentos automáticos.** «Implementar hasta 3 intentos totales ante errores recuperables, con `delay` de 1 s y 2 s, estado de intento, error definitivo y Reintentar manual conservando el modo; no usar `Thread.sleep` ni capturar `CancellationException`». | Propuso ampliar `CargaUi.Cargando` y añadir una función reutilizable de reintentos en el ViewModel, con estado y mensajes visibles en la pantalla. | Se dejó la detección de errores recuperables solo en `IOException`; se diferenció el error final por agotamiento de intentos; se impidió iniciar dos cargas simultáneamente y se conservó el modo para el reintento manual. | Evita reintentos indebidos, bloqueos o ejecuciones duplicadas y facilita saber por qué falló definitivamente la operación. |

## Prompts principales enviados a Gemini

A continuación se conserva el contenido principal de cada solicitud para facilitar la trazabilidad. Son versiones organizadas del texto de trabajo; las decisiones y correcciones efectivas están resumidas en la tabla anterior.

### Prompt 1 — Progreso real de primos

> Estoy desarrollando CentroTareas, una aplicación Android en Kotlin con Jetpack Compose, Material 3, corrutinas, StateFlow y arquitectura MVVM + Repository. Necesito implementar una mejora funcional: mostrar el progreso real del cálculo de números primos. Mantén el algoritmo en `Dispatchers.Default`; calcula el porcentaje según números realmente procesados, no mediante `delay`; actualiza el estado con `StateFlow`; conserva cancelación cooperativa con `ensureActive()`; al cancelar muestra el porcentaje alcanzado; al reiniciar comienza en 0 %; al terminar muestra cantidad y milisegundos. No crees otro proyecto, no alteres los otros módulos, respeta el diseño. Revisa los archivos existentes antes de generar códigos completos y usa comentarios mínimos.

### Prompt 2 — Comparación de rendimiento

> Continúa el proyecto CentroTareas en `mejora-ia`. Las cargas `Una por una` y `A la vez` tardan aproximadamente 5 y 3 segundos. Mide tiempos reales con `SystemClock.elapsedRealtime()` y guarda por separado los últimos tiempos exitosos con `StateFlow`; no los elimines al iniciar otro modo ni por errores. Incorpora a la tercera tarjeta una comparación con dos barras horizontales proporcionales, milisegundos, diferencia y porcentaje calculado a partir de los tiempos reales. Si falta un modo, indícalo; si el paralelo tarda más, informa el resultado real; evita dividir entre cero. Conserva errores, reintento, progreso de primos, cancelación y diseño sin iconos decorativos. Revisa los archivos antes de entregar los códigos completos, sin añadir dependencias.

### Prompt 3 — Reintentos automáticos

> Implementa exclusivamente la tercera mejora de CentroTareas: hasta 3 intentos totales por carga, ante errores recuperables como `IOException`, con espera progresiva de 1 y 2 segundos utilizando `delay`. Muestra `Intento N de 3`, indica la espera, conserva el indicador de carga y presenta un error definitivo tras agotar intentos. Mantén Reintentar manual para repetir el mismo modo que falló. No reintentes `CancellationException`, no uses `GlobalScope`, `runBlocking` ni `Thread.sleep`, y evita operaciones simultáneas. Conserva `async` y `coroutineScope`, el interruptor Simular error, los últimos tiempos exitosos y las otras dos tarjetas. No cambies el tema visual ni añadas dependencias. Revisa primero los archivos y entrega códigos completos para reemplazar.

## Pruebas y resultados observados

| Mejora | Prueba efectuada | Resultado observado |
|---|---|---|
| Progreso de primos | Calcular hasta 1 000 | 168 números primos; barra al 100 %. |
| Progreso de primos | Calcular hasta 5 000 000 | 348 513 números primos; barra al 100 %. |
| Progreso de primos | Calcular un límite grande y cancelar | Estado «Cancelado» y porcentaje alcanzado conservado. |
| Progreso de primos | Pulsar «Tócame» mientras se ejecuta una tarea | El contador siguió respondiendo. |
| Comparación de tiempos | Ejecutar `Una por una` y `A la vez` | 5 021 ms frente a 3 005 ms en una prueba; diferencia: 2 016 ms (40,2 % menos tiempo). |
| Comparación de tiempos | Activar «Simular error» después de mediciones exitosas | Se conservaron los tiempos anteriores en las barras. |
| Reintentos automáticos | Activar error simulado y ejecutar carga | Se observaron estados de intento y el mensaje final de fallo después de 3 intentos. |
| Reintentos automáticos | Desactivar error y repetir la carga | Se observó una carga exitosa; también se verificaron cargas secuencial y paralela normales. |

**Alcance de las verificaciones:** las capturas respaldan los resultados descritos; no sustituyen una prueba automatizada completa. Los tiempos varían según el emulador y la ejecución.

## Archivos y commits de la Fase 2

| Trabajo | Archivos principales | Commit |
|---|---|---|
| Rediseño visual previo | `ui/PantallaTareas.kt`, `ui/theme/Color.kt`, `ui/theme/Theme.kt` | `041e49f` — `style(mejora-ia): moderniza interfaz visual de CentroTareas` |
| Progreso real de primos | `model/Estados.kt`, `data/TareasRepository.kt`, `data/TareasRepositoryLocal.kt`, `viewmodel/TareasViewModel.kt`, `ui/PantallaTareas.kt` | `7111698` — `feat(mejora-ia): agrega progreso real al calculo de primos` |
| Comparación de tiempos | `viewmodel/TareasViewModel.kt`, `ui/PantallaTareas.kt` | `ce6364e` — `feat(mejora-ia): visualiza tiempos de carga` |
| Reintentos automáticos | `model/Estados.kt`, `viewmodel/TareasViewModel.kt`, `ui/PantallaTareas.kt` | `feat(mejora-ia): implementa reintentos automaticos` — confirmar hash en Git antes de la entrega. |

## Criterios técnicos respetados

- Arquitectura MVVM + Repository sin alterar la lógica de otras pantallas.
- Ninguna tarea larga ejecutada con `Thread.sleep` sobre el hilo principal en la versión final.
- Sin `GlobalScope` ni `runBlocking` en las mejoras.
- Cancelación cooperativa de cálculos con `ensureActive()` y propagación de `CancellationException`.
- Datos de perfil y pedidos ficticios; sin conexiones reales externas.
- Resultados y mediciones obtenidos durante la ejecución, no valores fijos para representar rendimiento.
