# Caja Aurea - POS Serverless

Sistema POS web para inventario, ventas y cierre de caja. El frontend esta publicado en GitHub Pages y el backend funciona con AWS API Gateway, Lambda y DynamoDB.

## Demo

```text
https://pablitopensante.github.io/aws/
```

Credenciales:

```text
Administrador: PablitoInPensante / DGGC1912
Cajero:        Caja / 123
```

## Arquitectura

- Frontend: HTML5, CSS propio y JavaScript vanilla modular.
- Publicacion web: GitHub Pages desde la rama `gh-pages`.
- Backend AWS: API Gateway + 2 Lambdas (`ProductsFunction`, `SalesFunction`).
- Base de datos: DynamoDB `ProductsTable` y `SalesTable`.
- Extra del sistema: `UsersTable` para usuarios y roles administrativos.

Se eligio JavaScript vanilla porque el proyecto demuestra directamente eventos del DOM, `fetch`, `async/await`, `try/catch`, box model, flexbox y grid.

## Estructura

```text
.kiro/specs/pos-frontend/     Specs SDD
docs/                         Frontend estatico publicado en Pages
frontend/                     Frontend local Spring Boot
backend/                      Backend local de desarrollo
serverless-pos/               Backend AWS SAM
```

## GitHub Pages

El sitio publicado vive en la rama:

```text
gh-pages
```

La fuente del sitio dentro de `main` esta en:

```text
docs/
```

`docs/config.js` apunta al API Gateway:

```text
https://jd9zmajy5h.execute-api.us-east-2.amazonaws.com
```

Para republicar Pages despues de cambiar `docs/`:

```bash
git subtree split --prefix docs -b gh-pages-publish
git push origin gh-pages-publish:gh-pages --force
git branch -D gh-pages-publish
```

## Ejecutar Local

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

Puertos:

```text
Frontend local: http://localhost:3000
Backend local:  http://localhost:8081
```

## Endpoints

Rutas requeridas por el examen:

```text
GET  /productos
POST /ventas
```

Rutas internas equivalentes:

```text
GET  /api/products
GET  /api/products/search?barcode=1
POST /api/sales
GET  /api/sales
```

## Proceso SDD

```text
.kiro/specs/pos-frontend/requirements.md
.kiro/specs/pos-frontend/design.md
.kiro/specs/pos-frontend/tasks.md
```

## Despliegue AWS

```bash
cd serverless-pos
sam build
sam deploy --guided
```

## Verificacion

```bash
./check-ready.sh
```

No subir `.env`, credenciales CSV, `node_modules`, `.aws-sam`, `target` ni archivos de sistema.
