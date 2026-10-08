# PROMPTS.md - Fase 2: calendario dinámico (rama mejora-ia)

Herramienta usada: Gemini en Android Studio.
Archivos modificados: FechaHoraScreen.kt, Formato.kt y ConfirmarCitaScreen.kt.

---

## Prompt 1: Próximos 5 días hábiles con LocalDate

**Prompt:**
Estoy en un proyecto Android con Jetpack Compose y Material 3. En FechaHoraScreen.kt, los días salen de Repositorio.diasFijos (lista de Strings ISO como "2026-10-08") y de un mapa privado diasSemanaFijos para el nombre del día. Reemplaza ambos por una función que, con java.time.LocalDate, devuelva los próximos 5 días hábiles a partir de hoy (sin sábados ni domingos) como List<LocalDate>. El nombre corto del día ("Lun", "Mar"...) debe salir de DayOfWeek con Locale("es", "PE"). El estado fechaSel debe seguir siendo un String ISO (usa toString()) para no romper Repositorio.horariosDisponibles(medicoId, fecha). No modifiques Repositorio.kt ni ConfirmarCitaScreen.kt.

**Respuesta resumida:**
Gemini eliminó el mapa diasSemanaFijos y el uso de Repositorio.diasFijos. Creó la función privada obtenerProximosDiasHabiles(cantidad = 5), que recorre desde LocalDate.now() saltando sábados y domingos hasta reunir 5 días. El nombre corto del día se obtiene con DayOfWeek.getDisplayName(TextStyle.SHORT, ...) en español, quitando el punto y poniendo la primera letra en mayúscula. La fecha seleccionada sigue guardándose como String ISO con toString(), y la lista se guarda con remember. Repositorio.kt y ConfirmarCitaScreen.kt no se modificaron.

**Qué corregí:**
No hice correcciones manuales al código. Como observación, la primera respuesta de Gemini solo describía los cambios en un resumen y el archivo no cambió hasta que se aplicó la modificación sobre el código; después de aplicarla, funcionó.

---

## Prompt 2: Flechas de semana y título dinámico del mes

**Prompt:**
Sobre FechaHoraScreen.kt (Jetpack Compose, Material 3), que ya usa obtenerProximosDiasHabiles() con LocalDate, agrega navegación por semanas:

1. Un estado "semana" (Int, empieza en 0) con rememberSaveable. Los días mostrados son los próximos 5 días hábiles contados desde LocalDate.now().plusWeeks(semana.toLong()). Cambia obtenerProximosDiasHabiles para que reciba la fecha de inicio como parámetro, y usa remember(semana) para la lista.
2. Junto al título del mes, dos IconButton con Icons.AutoMirrored.Filled.KeyboardArrowLeft y KeyboardArrowRight. La flecha derecha suma una semana y la izquierda resta una. La izquierda debe estar deshabilitada (enabled = false) cuando semana == 0, para no retroceder antes de la semana actual.
3. El título debe mostrar el nombre del mes y el año en español según los días mostrados ("Octubre 2026"), usando Locale.forLanguageTag("es-PE"). Si los 5 días pertenecen a dos meses distintos, muestra ambos ("Octubre - Noviembre 2026"; si cruza de año, "Diciembre 2026 - Enero 2027").
4. Al cambiar de semana, reinicia fechaSel y horaSel a null.

No modifiques Repositorio.kt ni ConfirmarCitaScreen.kt, y no cambies ChipDia ni ChipHora. Devuélveme el archivo completo con los cambios ya integrados.

**Respuesta resumida:**
Gemini agregó el estado semana (mutableIntStateOf con rememberSaveable) y cambió obtenerProximosDiasHabiles para recibir la fecha de inicio, usando LocalDate.now().plusWeeks(semana). Agregó dos IconButton junto al título; el izquierdo queda deshabilitado en la semana 0. El título calcula el mes del primer y último día mostrado y cubre tres casos: un solo mes, dos meses del mismo año y cambio de año. Al cambiar de semana se reinician fechaSel y horaSel. ChipDia, ChipHora, Repositorio.kt y ConfirmarCitaScreen.kt quedaron intactos.

**Qué corregí:**
Nada. El código funcionó sin cambios manuales. Probé avanzar semanas hasta el año 2030 y regresar: los títulos de mes cambian correctamente y la flecha izquierda queda inactiva en la semana actual.

---

## Prompt 3: Fecha en texto en español en la pantalla de confirmación

**Prompt:**
En mi proyecto Android con Jetpack Compose, el archivo Formato.kt (paquete ui.components) tiene la función formatearFecha(iso: String) que devuelve "08/10/2026". Agrega, en el mismo archivo, una función formatearFechaLarga(iso: String): String que reciba una fecha ISO ("2026-10-08") y devuelva el texto en español con el formato "Jueves 8 de octubre 2026" (día de la semana con la primera letra mayúscula, día del mes sin cero a la izquierda, mes en minúscula).

Usa java.time.LocalDate.parse y escribe los nombres de los días y de los meses en dos listas de texto dentro del código, en lugar de depender del Locale del teléfono, para que el resultado sea idéntico en todos los dispositivos. Para septiembre usa "setiembre". Si el texto no se puede interpretar como fecha, devuelve el texto original sin lanzar excepción.

Luego, en ConfirmarCitaScreen.kt, reemplaza SOLO el uso de formatearFecha(fecha) en la fila "Fecha" por formatearFechaLarga(fecha). No modifiques Repositorio.kt ni las demás pantallas. Devuélveme los dos archivos completos con los cambios ya integrados.

**Respuesta resumida:**
Gemini agregó en Formato.kt dos listas privadas (diasSemana y meses, con "setiembre") y la función formatearFechaLarga, que parsea con LocalDate.parse y arma "Jueves 8 de octubre 2026". Si la fecha no es válida, el try/catch devuelve el texto original. Se conservó formatearFecha para las pantallas Cita agendada y Mis citas. En ConfirmarCitaScreen.kt solo se cambió el import y la fila "Fecha".

**Qué corregí:**
Nada. Funcionó sin cambios manuales: la pantalla de confirmación muestra la fecha en texto largo y las demás pantallas siguen mostrando la fecha corta.

---

## Verificación de que ambas fases funcionan juntas

- Reservé una cita el 08/10 a las 09:00: ese horario dejó de aparecer para ese médico y fecha.
- Avancé varias semanas (hasta 2030) y regresé a la semana actual: el horario reservado seguía bloqueado.
- Con otra cuenta, el mismo horario sigue sin aparecer para ese médico (la reserva bloquea el horario para todos los pacientes), mientras que la cita solo aparece en "Mis citas" de quien la hizo.