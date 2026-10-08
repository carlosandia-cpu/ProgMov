@'
# Prompts de la mejora de Clínica Salud+

## Solicitud a Gemini

Resumen del prompt utilizado: reemplazar las tres fechas fijas de
AgendarCitaScreen.kt por hoy, mañana y pasado mañana, calculadas con Calendar
y mostradas en español. Mantener la selección única de fecha y hora,
el botón deshabilitado hasta elegir ambas opciones y el envío de esos datos
a la confirmación. Conservar remember y mutableStateOf, sin ViewModel.

## Revisión y correcciones

Revisé que la propuesta conservara el flujo hacia Confirmación y Mis citas.
En el emulador comprobé las tres fechas, el botón deshabilitado al seleccionar
solo una opción y su habilitación al seleccionar fecha y hora.
No registré una corrección manual adicional del código generado.
'@ | Set-Content -Path PROMPTS.md -Encoding UTF8

