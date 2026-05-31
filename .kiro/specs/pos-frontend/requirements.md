# Requirements

## Objetivo

Construir un frontend POS web para consultar productos y registrar ventas contra un API serverless en AWS. El proyecto usa HTML5, CSS y JavaScript vanilla para demostrar eventos, asincronismo, `fetch`, manejo de errores y separacion entre frontend y backend.

## Requisitos funcionales

1. La aplicacion debe mostrar una vista de login antes de permitir el acceso al POS.
2. La vista de productos debe consumir `GET /productos` o `GET /api/products` y mostrar nombre, precio, categoria y estado/cantidad disponible.
3. El POS debe permitir buscar productos por nombre, categoria o codigo/ID.
4. El campo de codigo debe aceptar operaciones rapidas como `1*24` o `1 x 24`, agregando el producto indicado con esa cantidad.
5. El carrito debe permitir agregar productos, cambiar cantidades, eliminar productos y calcular subtotal, descuento y total.
6. El flujo de venta debe registrar la venta con `POST /ventas` o `POST /api/sales`.
7. El payload de venta debe incluir `products`, `iva`, `subtotal`, `discount` y `total`.
8. La aplicacion debe mostrar confirmacion o comprobante cuando el API registre la venta.
9. La aplicacion debe mostrar mensajes de error si el API falla, retorna error o no se encuentra un producto.
10. La URL base del API Gateway debe estar centralizada en `frontend/src/main/resources/static/config.js`.

## Requisitos no funcionales

1. El HTML debe usar estructura semantica (`header`, `main`, `section`, formularios y listas).
2. El CSS debe usar grid/flexbox, box model y estilos propios.
3. El JavaScript debe usar modulos, eventos, `async/await`, promesas y `try/catch`.
4. El sistema debe ser operable con teclado, incluyendo flechas como navegacion de foco.
5. No se deben subir secretos, `.env`, `node_modules` ni archivos de sistema.
