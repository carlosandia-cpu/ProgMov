\# Laboratorio 02 - Carrito de Compras en Kotlin



\## Estudiante

Carlos Andia



\## Descripción



En este laboratorio se desarrolló un carrito de compras utilizando Kotlin. El programa permite registrar productos con nombre, precio y cantidad, calcular el subtotal, el IGV del 18% y el total de la compra.



También se implementó un reporte con el detalle de los productos, la identificación del producto más caro y una lógica de descuentos según el monto total de la compra.



\## Funciones implementadas



\- `calcularSubtotal()`: calcula el subtotal de todos los productos.

\- `calcularIGV()`: calcula el 18% del subtotal.

\- `calcularTotal()`: suma el subtotal y el IGV.

\- `mostrarDetalle()`: muestra los productos, cantidades e importes con formato.

\- `calcularDescuento()`: aplica un descuento según el total de la compra.



\## Resultado final



!\[Resultado final del programa](captura\_final.png)



\## ¿Por qué nombre y precio son val pero cantidad es var?



`nombre` y `precio` se declaran con `val` porque sus valores no deberían cambiar después de crear el producto. En cambio, `cantidad` se declara con `var` porque puede modificarse si el cliente aumenta o disminuye la cantidad de un producto en el carrito.



\## ¿Qué pasaría si intentas cambiar el precio después de crear el producto?



No sería posible modificar directamente el precio porque fue declarado utilizando `val`. Kotlin mostraría un error indicando que el valor no puede ser reasignado.

