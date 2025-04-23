# Book API

Este proyecto es una **API RESTful de gestión de libros** creada con **Spring Boot 3**, generada desde una especificación **OpenAPI 3.0.1**, que integra:

- **OpenAPI Generator** (versión 7.12) para generar el esqueleto de la API (interfaces y modelos).
- **Liquibase** para versionar y aplicar cambios en la base de datos.
- **H2 en memoria** como base de datos de desarrollo y pruebas.
- **Validación de ISBN** mediante la API pública de OpenLibrary (https://openlibrary.org).
- **Swagger UI** para explorar y probar los endpoints.
- **Consola H2** para inspeccionar la base de datos en tiempo de ejecución.

---

## Documentación de la API
| Método | Endpoint       | Descripción                         |
|--------|----------------|-------------------------------------|
| GET    | `/books`       | Devuelve todos los libros           |
| POST   | `/books`       | Agregar un nuevo libro              |
| GET    | `/books/{id}`  | Devuelve un libro por ID            |
| PUT    | `/books/{id}`  | Actualiza un libro existente por ID |
| DELETE | `/books/{id}`  | Elimina un libro por ID             |

##  Pre-requisitos

- Java 17 o superior
- Maven 3.6+
- Conexión a Internet (para validar ISBN vía OpenLibrary)

---

##  Instalación y generación de código

1. Clona este repositorio:
   ```bash
      ```bash
   git clone https://github.com/Maria-Avila10/book_api.git
   cd book_api
   ```
##  Genera las fuentes de OpenAPI:
  ```bash
      ```bash
  mvn clean generate-sources
  mvn spring-boot:run
```
---
- La aplicación arrancará en http://localhost:8080.
---
## Licencia

Este proyecto está bajo la **Licencia MIT**.