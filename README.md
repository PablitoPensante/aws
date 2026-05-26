# Kiro Fullstack

Proyecto combinado con:

- `backend/`: API de ventas Spring Boot/SAM.
- `frontend/`: frontend Spring Boot con HTML/CSS/JS estático.

## Puertos locales

- Backend ventas: `http://localhost:8081`
- Frontend: `http://localhost:3000`

En modo local, el backend usa `frontend/data/productos.json` como catálogo de productos. Así el POS puede buscar productos y registrar ventas sin desplegar DynamoDB.

## Ejecutar

Desde esta carpeta:

```bash
./run-local.sh
```

O por separado:

```bash
cd backend
mvn spring-boot:run
```

```bash
cd frontend
mvn spring-boot:run
```

Abre:

```text
http://localhost:3000/login.html
```

El POS llama al backend en:

```text
http://localhost:8081/api/products
http://localhost:8081/api/sales
```

## Modos

Modo local:

```bash
./configure-frontend.sh local
```

Modo AWS/serverless, despues de desplegar `serverless-pos`:

```bash
./configure-frontend.sh serverless https://TU_API.execute-api.REGION.amazonaws.com
```

## Verificar todo

```bash
./check-ready.sh
```

## Presentacion

Documentos listos para exponer:

```text
PRESENTACION.md
GUIA-DEMO.md
```

## Pantallas

```text
http://localhost:3000/login.html
http://localhost:3000/productos.html
http://localhost:3000/pos.html
http://localhost:3000/ventas.html
```

Usuario demo:

```text
PablitoInPensante / DGGC1912
```

En el POS presiona `?` o el boton `Atajos` para ver el modo aprendizaje.

## Flujo completo

1. Crear o revisar productos en `productos.html`.
2. Vender desde `pos.html`.
3. Confirmar pago y recibo.
4. Revisar la venta guardada en `ventas.html`.
5. Para AWS, desplegar `serverless-pos`, sembrar DynamoDB y cambiar el frontend con `configure-frontend.sh serverless`.
