# Proyecto Pablito - Fullstack POS

Proyecto POS con frontend HTML5/CSS/JavaScript, backend local Spring Boot y backend serverless AWS SAM.

## Arquitectura

- `frontend/`: paginas HTML semanticas, CSS propio y JavaScript vanilla modular.
- `backend/`: API local de desarrollo en Spring Boot.
- `serverless-pos/`: API Gateway + 2 Lambdas (`ProductsFunction`, `SalesFunction`) + DynamoDB `ProductsTable` y `SalesTable`.
- `UsersTable`: tabla adicional para usuarios/roles administrativos.

Se eligio JavaScript vanilla porque el examen evalua fundamentos: eventos, asincronismo con `async/await`, consumo API con `fetch`, manejo de errores con `try/catch`, box model, flexbox y grid.

## Estructura

```text
.kiro/specs/pos-frontend/
backend/
frontend/
serverless-pos/
```

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

Abrir:

```text
http://localhost:3000/login.html
```

Credenciales:

```text
PablitoInPensante / DGGC1912
Caja / 123
```

El POS llama al backend local en:

```text
http://localhost:8081/api/products
http://localhost:8081/api/sales
```

El backend serverless tambien expone las rutas del examen:

```text
GET  /productos
POST /ventas
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

## Proceso SDD

```text
.kiro/specs/pos-frontend/requirements.md
.kiro/specs/pos-frontend/design.md
.kiro/specs/pos-frontend/tasks.md
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

## Capturas Para Entrega

- `productos.html` con productos cargados desde API.
- `pos.html` registrando una venta exitosa.
- `ventas.html` o comprobante con respuesta del API.
- Error controlado cuando la API falle o el codigo no exista.

## Flujo completo

1. Crear o revisar productos en `productos.html`.
2. Vender desde `pos.html`.
3. Confirmar pago y recibo.
4. Revisar la venta guardada en `ventas.html`.
5. Para AWS, desplegar `serverless-pos`, sembrar DynamoDB y cambiar el frontend con `configure-frontend.sh serverless`.

## Despliegue AWS

```bash
cd serverless-pos
sam build
sam deploy --guided
```

No subir `.env`, credenciales CSV, `node_modules`, `.aws-sam`, `target` ni archivos de sistema.

## GitHub Pages

La carpeta publicable es:

```text
docs/
```

En GitHub, activar Pages con:

```text
Settings -> Pages -> Deploy from a branch -> main -> /docs
```

URL esperada:

```text
https://pablitopensante.github.io/aws/
```

`docs/config.js` apunta al backend AWS:

```text
https://jd9zmajy5h.execute-api.us-east-2.amazonaws.com
```

El login de Pages crea una sesion local para `PablitoInPensante` o `Caja`; las operaciones de productos y ventas se hacen contra API Gateway.
