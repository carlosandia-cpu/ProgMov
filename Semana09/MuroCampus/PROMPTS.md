# PROMPTS – MuroCampus

**Rama:** `mejora-ia`  
**Herramienta:** Gemini de Android Studio

Los prompts corresponden a las instrucciones utilizadas para las tres mejoras; las respuestas de Gemini se presentan resumidas.

| Prompt que escribí | Qué respondió Gemini | Qué cambié o descarté | Por qué |
|---|---|---|---|
| Analiza MuroCampus con Kotlin, Compose, Retrofit y MVVM + Repository. Implementa búsqueda por título con StateFlow, filtrado local sin peticiones HTTP, sin distinguir mayúsculas, combinable con el filtro por usuario, mensaje Sin coincidencias y limpieza. Mantén los estados y las funciones existentes. Modifica solo MuroCampus, sin commits automáticos. | Modificó PostsViewModel.kt, PantallaLista.kt y app/build.gradle.kts. Agregó busquedaQuery, postsOriginales, buscar(), contains(ignoreCase = true), OutlinedTextField e iconos. Informó compilación exitosa. | Se conservaron los cambios. Se revisó que buscar() solo actualice la lista cuando el estado es Exito. Se resolvieron las marcas rojas del IDE mediante la reconstrucción/sincronización del proyecto, sin reescribir la lógica. | La búsqueda debe operar en memoria, no generar tráfico HTTP ni sustituir los estados Cargando o Error. La dependencia de iconos permite usar Search y Clear. La compilación forzada fue exitosa y se probó con Logcat. |
| Implementa un interceptor de OkHttp en RetrofitClient.kt que agregue Accept: application/json a todas las peticiones. Conserva el logging, coloca el interceptor antes del logging, evita duplicados y mantén Retrofit, Kotlinx Serialization e ignoreUnknownKeys. Modifica solo ese archivo, sin commits automáticos. | Creó acceptHeaderInterceptor usando newBuilder().header("Accept", "application/json"). Lo registró antes de HttpLoggingInterceptor y conservó la URL HTTPS y el conversor JSON. Informó compilación exitosa. | Se conservó la propuesta según la explicación y se comprobó el encabezado en Logcat. | header() reemplaza los valores previos del mismo encabezado, evitando duplicados. El orden permite registrar la petición modificada. |
| Implementa eliminación con @DELETE("posts/{id}"), @Path y Response<Unit>. Agrega Result al Repository, StateFlow y viewModelScope al ViewModel, botón y diálogo de confirmación en el detalle. Maneja carga, éxito, error y reintento; evita duplicados, regresa a la lista y retira el post localmente al tener éxito. Mantén las mejoras anteriores y MVVM + Repository. Considera que JSONPlaceholder simula el borrado. No hagas commits automáticos. | Modificó PostApi.kt, PostRepository.kt, PostsViewModel.kt, PantallaDetalle.kt y MainActivity.kt. Agregó eliminar(), estado reactivo, comprobación isSuccessful, confirmación, reintento y callback de navegación. Informó compilación exitosa. | Se mantuvo la propuesta y se probó el diálogo, la eliminación local, la petición DELETE y la búsqueda posterior. Queda verificar el código HTTP de respuesta, la cancelación y el reintento sin internet. | La API debe estar aislada de la UI y el Repository debe manejar el resultado HTTP. La confirmación evita borrados accidentales; la lista solo debe cambiar tras el éxito. El borrado no es persistente en JSONPlaceholder. |

## Análisis técnico

- La búsqueda utiliza datos ya descargados y no necesita nuevas solicitudes HTTP.
- El interceptor centraliza el encabezado sin modificar cada endpoint.
- La eliminación añade una operación a la API y al Repository, cumpliendo el requisito de modificar estas capas en al menos una mejora.
- Se revisó directamente el código del buscador. Para las otras dos mejoras se dispone de la explicación de Gemini y las pruebas visuales; queda revisar el diff completo para confirmar que no haya acceso directo a Retrofit desde el ViewModel o la pantalla y que se conserve la cancelación de corrutinas.
