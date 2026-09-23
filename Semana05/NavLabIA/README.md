# NavLabIA — aplicación creada con IA

Proyecto de la Semana 05 generado con Gemini en Android Studio. El objetivo fue reproducir la aplicación de navegación original a partir de un prompt, para compararla con la versión realizada manualmente en `NavLab`.

## Prompt enviado a Gemini

```text
Crea desde cero la aplicación completa NavLabIA en este proyecto Android vacío, usando Kotlin, Jetpack Compose y Navigation Compose. El resultado debe reproducir lo más fielmente posible la app original NavLab de las capturas adjuntas y de la página 17 del PDF. Usa las imágenes solo como referencia visual; las funciones exactas están descritas aquí. No leas ni copies archivos del proyecto hermano Semana05/NavLab: esta versión debe generarse a partir de este prompt.

CONFIGURACIÓN
- El package de este proyecto es com.example.navlabia.
- Añade la dependencia necesaria de Navigation Compose en app/build.gradle.kts y sincroniza Gradle.
- Organiza el código en MainActivity.kt, navigation/Screen.kt, navigation/AppNavigation.kt y cuatro archivos dentro de screens: HomeScreen.kt, ListScreen.kt, DetailScreen.kt y ProfileScreen.kt.
- MainActivity debe mostrar AppNavigation() dentro del tema de este proyecto.
- Screen debe definir las rutas home, list, profile y detail/{itemId}. Detail debe incluir una función para construir la ruta con un ID.
- AppNavigation debe usar rememberNavController y NavHost con inicio en home. Declara itemId con NavType.IntType y entrégalo a DetailScreen como Int.

PANTALLAS Y ASPECTO ORIGINAL
1. Inicio: fondo claro, contenido centrado verticalmente, texto «Pantalla Tecsup», botón relleno «Ver lista de elementos» y debajo botón con borde «Mi perfil». Ambos ocupan prácticamente el ancho disponible y navegan a sus pantallas. Mantén un diseño sencillo como el original; no uses el diseño morado mejorado.
2. Lista: TopAppBar con título «Lista» y flecha visible para regresar. Muestra inicialmente ocho filas, de «Elemento número 1» a «Elemento número 8», cada una con el subtítulo «Toca para ver el detalle» y un separador. Al pulsar una fila, navega a su detalle pasando su número como itemId. Cada fila tiene además un botón «Eliminar» que la quita de la lista sin abrir el detalle. Usa estado observable de Compose para que la lista se actualice.
3. Detalle: TopAppBar con título «Detalle del elemento» y flecha para regresar. Debajo muestra «Elemento #N» y una tarjeta con «ID recibido: N» y una breve explicación de que el valor llegó como argumento Int desde el NavHost. N es el itemId recibido.
4. Perfil: contenido centrado con «Mi Perfil», «Estudiante Tecsup» y un botón «Ir al inicio». Ese botón vuelve a home sin acumular copias de home en el historial.

Mantén los colores claros y botones azul grisáceo de la versión original. No agregues inicio de sesión, perfiles académicos, pantallas adicionales, nuevas funciones ni dependencias innecesarias.

Aplica todos los cambios directamente en NavLabIA. Después compila el proyecto, corrige cualquier error y dime qué archivos modificaste y si la compilación terminó correctamente.
```

## Resultado

Gemini generó las rutas de Navigation Compose y las pantallas Inicio, Lista, Detalle y Perfil. La lista muestra ocho elementos, permite abrir su detalle mediante un ID entero y ofrece la acción de eliminar. La aplicación se ejecutó en el emulador Pixel 9.

## Comparación

- `NavLab`: implementación original realizada manualmente.
- `NavLabIA`: recreación generada a partir del prompt anterior.
- `mejora-ia`: versión con mejoras visuales de la aplicación original.