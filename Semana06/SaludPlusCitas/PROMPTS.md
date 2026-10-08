# PROMPTS.md - Fase 2: calendario dinámico (rama mejora-ia)

Herramienta usada: Gemini en Android Studio.

## Prompt 1: Próximos 5 días hábiles con LocalDate

**Prompt:**
Estoy en un proyecto Android con Jetpack Compose y Material 3. En FechaHoraScreen.kt, los días salen de Repositorio.diasFijos (lista de Strings ISO como "2026-10-08") y de un mapa privado diasSemanaFijos para el nombre del día. Reemplaza ambos por una función que, con java.time.LocalDate, devuelva los próximos 5 días hábiles a partir de hoy (sin sábados ni domingos) como List<LocalDate>. El nombre corto del día ("Lun", "Mar"...) debe salir de DayOfWeek con Locale("es", "PE"). El estado fechaSel debe seguir siendo un String ISO (usa toString()) para no romper Repositorio.horariosDisponibles(medicoId, fecha). No modifiques Repositorio.kt ni ConfirmarCitaScreen.kt.
**Respuesta resumida:**
Gemini eliminó el mapa diasSemanaFijos y el uso de Repositorio.diasFijos, y creó
la función obtenerProximosDiasHabiles(cantidad = 5) que recorre desde
LocalDate.now() saltando sábados y domingos. El nombre corto del día sale de
DayOfWeek con Locale es-PE, y la fecha seleccionada se sigue guardando como
String ISO para no romper Repositorio.horariosDisponibles.

