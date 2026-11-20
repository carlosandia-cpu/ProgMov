# PROMPTS.md — CampusGPS

## Laboratorio 13 — Fase 2 con IA

**Proyecto:** CampusGPS  
**Rama:** mejora-ia  
**Tecnologías:** Kotlin, Jetpack Compose, Google Maps Compose, StateFlow y MVVM + Repository  
**Herramienta de IA:** Gemini

## 1. Objetivo

Utilizar inteligencia artificial como herramienta de apoyo para desarrollar tres mejoras funcionales sobre CampusGPS, manteniendo la arquitectura existente y verificando manualmente el código generado.

## 2. Registro de prompts

| Prompt utilizado | Respuesta de Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| **Mejora 1:** Implementar filtros por categoría mediante FilterChip, utilizando StateFlow y actualizando simultáneamente mapa y lista. | Propuso modificar MapaViewModel, PantallaMapa y ListaLugares para filtrar los destinos. | Se ajustó el borde de FilterChip mediante BorderStroke y se reemplazó el texto «Ver los 5» por «Ver lugares». | Mejorar la compatibilidad con Material 3 y evitar textos incorrectos cuando existen filtros activos. |
| **Mejora 2:** Agregar un radio de cercanía de 100, 200 y 500 metros, dibujando un círculo sobre el mapa y combinándolo con las categorías. | Propuso utilizar StateFlow para el radio, Circle para el mapa y filtros combinados en el ViewModel. | Se ajustó la validación del destino seleccionado y el mensaje cuando todavía no existe ubicación GPS. | Evitar selecciones inválidas y resultados confusos cuando no hay información de ubicación. |
| **Mejora 3:** Implementar un mapa oscuro mediante MapStyleOptions, conservando marcadores, filtros y radio de cercanía. | Propuso un archivo JSON de estilo oscuro, un botón de alternancia y rememberSaveable para conservar el estado. | Se eliminó una función duplicada de bienvenida, se revisó el manejo de errores del estilo y se adaptaron los paneles y tarjetas al modo oscuro. | Evitar duplicación de código, facilitar el diagnóstico y mantener una interfaz visualmente consistente. |

## 3. Decisiones técnicas

### Separación de responsabilidades

El filtrado de lugares y la selección del radio se mantienen en el ViewModel. Los Composables únicamente presentan la información y notifican las acciones del usuario.

### Conservación de la funcionalidad

Se priorizó no modificar la lógica original de permisos, ubicación y Repository durante la incorporación de mejoras visuales.

### Revisión del código generado

No se aceptaron automáticamente todas las propuestas de Gemini. Se realizaron ajustes para mantener la compatibilidad, evitar código duplicado y conservar el funcionamiento de los módulos anteriores.

## 4. Pruebas realizadas

**Mejora 1:** se comprobó que las categorías muestran los destinos correspondientes y actualizan los marcadores del mapa.

**Mejora 2:** se verificaron los radios de 100, 200 y 500 metros, confirmando que la cantidad de destinos varía según la distancia.

**Mejora 3:** se comprobó la alternancia entre mapa claro y oscuro, manteniendo visibles los marcadores y el círculo de cercanía.

**Validación pendiente:** confirmar la compilación final y los cambios visuales de los paneles oscuros antes de la entrega.

## 5. Conclusión

La inteligencia artificial permitió proponer e implementar mejoras sobre una aplicación funcional. La revisión manual fue necesaria para adaptar el código al proyecto, corregir detalles de compatibilidad y respetar la arquitectura MVVM + Repository.

La implementación demuestra que la IA puede apoyar el desarrollo de software, pero sus resultados requieren validación técnica antes de integrarse definitivamente.