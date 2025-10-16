# API de franquicias

API RESTful reactiva para la gestión de franquicias, sucursales y productos.

## Tecnologías utilizadas

* **Lenguaje:** Java 21
* **Framework:** Spring Boot con Spring WebFlux
* **Acceso a datos:** Spring Data R2DBC
* **Documentación:** SpringDoc (Swagger UI)

## ¿Cómo ejecutar la API de manera local?

### 1. Prerrequisitos:

* JDK 21
* Gradle
* Docker
* Git

### 2. Clonar el repositorio:

Abre la terminal y ejecuta el siguiente comando:

```bash
git clone git@github.com:JuanDCeballos/franchises-api-nequi.git
cd franchises-api-nequi
```

### 3. Levantar la base de datos de prueba con Docker

El siguiente comando usará Docker para descargar y ejecutar un contenedor de PostgreSQL preconfigurado para esta
aplicación.

```bash
docker run --name franchises_db_local -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=password123 -e POSTGRES_DB=franchises_api_db -p 5432:5432 -d postgres:15-alpine
```

### 4. Ejecutar la aplicación

**Con Gradle:**

```bash
./gradlew bootRun
```

### 5. Verificar y probar

Para interactuar con los endpoints y ver toda la documentación de la API, abre tu navegador en la siguiente URL:

➡️ **[http://localhost:8080/webjars/swagger-ui/index.html](http://localhost:8080/webjars/swagger-ui/index.html)**

# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

### Información tomada de:

[Scaffold Clean Architecture](https://bancolombia.github.io/scaffold-clean-architecture/docs/intro)

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando
con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el
artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio
mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de
aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de
entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de
diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las
dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias
concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde
encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**
