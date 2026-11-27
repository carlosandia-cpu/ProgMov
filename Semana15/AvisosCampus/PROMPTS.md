# PROMPTS.md — AvisosCampus

Rama: `mejora-ia`. Registro de asistencia de IA y verificación técnica.

| Prompt que escribí | Qué respondió la IA | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Gemini: implementar canal Urgentes con IMPORTANCE_HIGH; usarlo para recordatorios y conservar MVVM. | Modificó Modelos.kt y Notificador.kt. Definió esUrgente y seleccionó el canal según el origen. | Se revisó git diff y se conservó la propuesta. | No es necesario introducir Context en el ViewModel. ADB confirmó las importancias 3 y 4. |
| ChatGPT: proporcionar código completo para la mejora 2 (BigTextStyle). | Propuso BigTextStyle en Notificador y un texto más largo en AvisosViewModel. | Se aplicó el código y se expandió la notificación. | Permite leer el texto completo sin cambiar los canales ni el PendingIntent. |
| ChatGPT: proporcionar código completo para la mejora 3 (receptor del cargador). | Propuso CargadorReceiver, origen CARGADOR, StateFlow y DisposableEffect. | Se creó el receptor y se integraron los dos estados. Se simuló conexión y desconexión. | El ViewModel no contiene Context y el receptor se retira en onDispose. Ambas notificaciones y el historial se verificaron. |

## Evidencias y commits

- Canal Urgentes: `bebe474`; ADB confirmó importancia 4 (Urgentes) y 3 (Avisos).
- BigTextStyle: `5701840`; el texto se visualizó completo en el panel.
- Cargador: se comprobó conexión y desconexión en notificaciones e historial; consultar el hash real en el historial de la rama.

Gemini se utilizó para la primera mejora; ChatGPT para las mejoras 2 y 3. No se atribuyen respuestas a Gemini que no se hayan observado. El diseño adicional no se realizó.

[Historial de mejora-ia](https://github.com/carlosandia-cpu/ProgMov/commits/mejora-ia/Semana15/)
