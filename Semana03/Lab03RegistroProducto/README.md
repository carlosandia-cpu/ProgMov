# Lab03 - Registro de Producto

## Autor

Carlos Andia

## Descripción

Aplicación Android desarrollada en Kotlin utilizando Jetpack Compose.  
Permite ingresar el nombre de un producto, su precio y cantidad. Al presionar el botón **AGREGAR PRODUCTO**, la aplicación muestra una Card con el resumen de los datos ingresados y calcula automáticamente el importe total mediante:

**Importe = Precio × Cantidad**

La interfaz utiliza componentes de Material 3 y aplica espaciado, jerarquía tipográfica, colores del tema y manejo de estado con `remember` y `mutableStateOf`.

## Pantalla inicial

<img width="1556" height="817" alt="image" src="https://github.com/user-attachments/assets/7ae63249-672f-44d0-99d9-e4058ca73ad3" />

## Producto registrado

<img width="727" height="781" alt="image" src="https://github.com/user-attachments/assets/ed7b708b-c4ae-4e25-ba1b-8ff4f2059b28" />

## ¿Qué pasaría si las variables de los campos se declaran sin remember?

Si se elimina `remember`, el estado de los campos se vuelve a crear cada vez que Jetpack Compose realiza una recomposición. Esto provoca que los valores ingresados no se conserven correctamente y los campos puedan volver a su valor inicial.

`remember` permite conservar el estado durante las recomposiciones, por lo que el texto escrito por el usuario permanece visible en los campos.
