# Lab04 - Carrito TECSUP

Aplicación Android desarrollada con Kotlin y Jetpack Compose para registrar productos en una lista observable y calcular los totales de un carrito de compras.

## Pregunta de la Etapa 1

### ¿Por qué la lista se declara con `val` y aun así podemos agregarle elementos?

La variable se declara con `val` porque la referencia a la lista no será reemplazada por otra lista. Sin embargo, `mutableStateListOf` crea una colección mutable, por lo que su contenido sí puede modificarse agregando o eliminando productos.

Además, Jetpack Compose observa los cambios realizados en esta lista y actualiza automáticamente los componentes de la interfaz que dependen de ella.