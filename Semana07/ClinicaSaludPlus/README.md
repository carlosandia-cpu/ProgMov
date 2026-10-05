# Clínica Salud+ — Opción A

Aplicación Android desarrollada con Kotlin y Jetpack Compose para consultar médicos y reservar citas. Forma parte de la actividad integradora de las semanas 1 a 6.

## Requisitos funcionales

1. Consultar médicos disponibles y filtrarlos por especialidad.
2. Abrir el perfil del médico seleccionado y conservar sus datos durante la navegación.
3. Elegir una fecha y una hora para agendar una cita.
4. Confirmar la reserva y consultar su resumen.
5. Consultar las citas desde el menú lateral, diferenciando los estados Confirmada y Completada.

## Flujo principal

Inicio → Perfil del médico → Agendar cita → Confirmación → Mis citas.

Para confirmar es obligatorio seleccionar **una fecha y una hora**. Cada grupo permite una sola opción elegida.

## Navegación secundaria

El menú lateral (`drawer`) permite acceder a Inicio, Mis citas, Historial médico y Perfil.

## Componentes

- `MainActivity.kt`: estado y navegación entre pantallas.
- `Medico.kt` y `Cita.kt`: modelos y datos de demostración.
- `InicioScreen.kt`: especialidades en `LazyRow` y médicos en `LazyColumn`.
- `PerfilMedicoScreen.kt`: detalle del médico seleccionado.
- `AgendarCitaScreen.kt`: selección de fecha y hora.
- `ConfirmacionScreen.kt`: resumen de la reserva.
- `MisCitasScreen.kt`: lista de citas y estados.
- `SeccionScreen.kt`: contenido de la sección Perfil.

Las pantallas utilizan `Scaffold` con barra superior y padding del contenido. El estado se maneja con `remember` y `mutableStateOf`; no se utiliza ViewModel ni MVVM.

## Cómo ejecutar

1. Abrir `Semana05/ClinicaSaludPlus` en Android Studio.
2. Esperar la sincronización de Gradle.
3. Seleccionar un emulador o dispositivo Android.
4. Ejecutar la configuración `app`.

## Prueba realizada

Se abrió el perfil de un médico, se seleccionó una fecha y una hora, se confirmó la cita y se comprobó su aparición en **Mis citas** con estado **Confirmada**. La lista también muestra una cita **Completada** de ejemplo.

## Alcance de los datos

Los médicos, fechas y horas son datos de demostración. Las nuevas citas se conservan en memoria mientras la aplicación permanece abierta; al reiniciarla se restablece la lista inicial. No se consulta una agenda médica real.

## Repositorio

[Ver Clínica Salud+ en GitHub](https://github.com/carlosandia-cpu/ProgMov/tree/main/Semana05/ClinicaSaludPlus)

## Fases de desarrollo

- `main`: implementación inicial de la Opción A.
- `mejora-ia`: fase de mejoras asistidas por IA; se documentará allí el prompt final, los cambios realizados y sus capturas.