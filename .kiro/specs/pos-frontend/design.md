# Design

## Arquitectura

El sistema separa responsabilidades en tres capas:

- Frontend: HTML5, CSS y JavaScript vanilla servidos por Spring Boot desde `frontend/src/main/resources/static`.
- Backend local: Spring Boot en `backend/` para pruebas locales.
- Backend AWS: API Gateway + Lambda + DynamoDB definido en `serverless-pos/template.yaml`.

## Framework elegido

Se usa JavaScript vanilla porque el examen valora la comprension de los fundamentos: DOM, eventos, `fetch`, asincronismo y CSS propio. Para este POS no se necesita un framework pesado; los modulos ES permiten separar responsabilidades sin agregar build tools.

## Componentes frontend

- `login.html`, `login.js`: autenticacion y sesion local.
- `productos.html`, `productos.js`: listado y gestion de productos.
- `pos.html`, `pos.js`: pantalla principal de venta.
- `ventas.html`, `ventas.js`: historial de ventas.
- `modules/api.js`: cliente HTTP centralizado.
- `modules/cart.js`: estado del carrito.
- `modules/search.js`: busqueda por nombre/categoria.
- `modules/scanner.js`: busqueda por codigo/ID y expresiones `ID*cantidad`.
- `modules/payment.js`: registro de pago y envio al API.
- `modules/receipt.js`: comprobante de venta.

## Contrato de API

La URL base se configura en `config.js`.

Endpoints principales:

- `GET /productos` y `GET /api/products`: lista productos.
- `GET /api/products/search?barcode={codigo}`: busca por codigo.
- `POST /ventas` y `POST /api/sales`: registra venta.
- `GET /ventas` y `GET /api/sales`: lista ventas.

Formato de venta:

```json
{
  "products": [
    {
      "productId": 1,
      "productName": "Arroz Premium",
      "productPrice": 5200,
      "productCost": 0,
      "quantity": 2
    }
  ],
  "iva": 0,
  "subtotal": 10400,
  "discount": 0,
  "total": 10400
}
```

## Modelo DynamoDB

- `ProductsTable`: clave `id`, guarda `name`, `barcode`, `unitPrice`, `availableStock`, `category`.
- `SalesTable`: clave `id`, guarda `products`, `subtotal`, `discount`, `total`, `payment`, `cashier`, `createdAt`.
- `UsersTable`: clave `usuario`, guarda usuarios y roles del sistema.
