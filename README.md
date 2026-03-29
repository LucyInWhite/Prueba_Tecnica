# 🚀 Prueba Técnica: API REST de Gestión de Usuarios

La API permite la gestión de usuarios, integrando seguridad, validaciones personalizadas y documentación automatizada.

---

## 🛠️ Tecnologías y Versiones
*   **Lenguaje:** Java 17 (Amazon Corretto / Eclipse Temurin)
*   **Framework:** Spring Boot 3.2.4
*   **Documentación:** SpringDoc OpenAPI 2.1.0 (Swagger UI)
*   **Gestor de Dependencias:** Maven 3.9.14 (vía Maven Wrapper)
*   **Contenedorización:** Docker 🐳
*   **Seguridad:** Encriptación de credenciales integrada.

---
## 📂 Estructura del Proyecto

El proyecto sigue una organización por capas para facilitar el mantenimiento y la escalabilidad:

```text
src/main/java/com/example/prueba_tecnica/
├── controller/
│   └── UserController.java          # Endpoints de la API REST.
├── exception/
│   └── GlobalExceptionHandler.java   # Captura y formato de errores específicos (RFC).
├── model/
│   ├── Address.java                 # Entidad/Objeto de dirección.
│   ├── LoginRequest.java            # DTO para autenticación.
│   └── User.java                    # Entidad principal de Usuario.
├── service/
│   └── UserService.java             # Lógica de negocio y procesamiento de datos.
├── util/
│   └── EncryptionUtil.java          # Utilidad para la encriptación de contraseñas.
└── PruebaTecnicaApplication.java    # Clase principal de arranque.

src/test/java/
└── com.example.prueba_tecnica/
    └── PruebaTecnicaApplicationTests # Pruebas unitarias y de integración.
raiz
├── .env                         # Archivo de variables de entorno (Secret Key) [Ignorado en Git].
├── docker-compose.yml           # Orquestación de contenedores y configuración de red interna.
├── Dockerfile                   # Receta para construir la imagen aislada de Docker.
├── OpenApi.json                 # Contrato de la API generado por Swagger.
├── pom.xml                      # Archivo principal de configuración y dependencias (Maven).
└── PostmanCollection.json       # Colección exportada con casos de prueba lista para Postman.

```
---
## 💡 Decisiones de Arquitectura e Implementación

Para garantizar la escalabilidad y robustez del sistema, se aplicaron los siguientes criterios técnicos y de negocio:

---



### 1. 🏗️ Arquitectura y Persistencia
*   **Diseño en Capas:** Se implementó el patrón **Controller-Service-Repository**. Esta separación de responsabilidades asegura que la lógica de negocio esté aislada de la entrada de datos y del acceso a la base de datos.
*   **Base de Datos Relacional (MySQL):** Se integró MySQL a través de contenedores para simular un entorno productivo real, garantizando la persistencia y la integridad referencial de los datos.  
* **Validación de Datos y Reglas de Negocio:** Uso de `jakarta.validation` para interceptar datos incorrectos antes de la persistencia:
    *   **Direcciones Diferenciadas:** Se estableció que la **dirección de casa sea obligatoria**, garantizando un punto de contacto base para el usuario, mientras que la **dirección de trabajo es opcional**, brindando flexibilidad para perfiles laborales independientes o desempleados.
    *   **Formatos Estrictos:** Validación de mayoría de edad, estructura de email y campos requeridos para asegurar la integridad de cada registro.

---
### 2. ⚙️ Configuración de Variables de Entorno

Para que el proyecto funcione correctamente, es necesario crear un archivo llamado `.env` en la raíz del proyecto. Este archivo debe contener las siguientes variables:

```properties
SECRET_KEY=
DB_NAME=
DB_USER=                  (Tus datos)
DB_PASS=
SPRING_DATASOURCE_URL:
```
---

### 3. 🌍 Gestión de Tiempos (Caso Madagascar)
*   **Integridad vs. Formato:** Aunque el requerimiento pedía precisión hasta minutos, el sistema **almacena los segundos** internamente. Esto previene la pérdida de precisión histórica, mientras que la salida (output) se formatea estrictamente según lo solicitado.
*   **Resiliencia Horaria:** La visualización se configuró en **UTC referenciando a Antananarivo**. Esta decisión técnica garantiza que, ante cualquier cambio geopolítico en el huso horario de Madagascar, la referencia geográfica permanezca constante y rastreable.

---

### 4. 🔍 Lógica de Filtrado Inteligente (Teléfonos)
Se detectó que los registros internacionales inician con el carácter `+` (codificado como `%2B` en solicitudes URL). Se implementó una lógica dual:

| Caso de Uso | Prefijo      | Comportamiento |
| :--- |:-------------| :--- |
| **Búsqueda por Lada** | `+` (`%2B`)  | El sistema filtra priorizando el código de país (ej. `+52`). |
| **Búsqueda General** | Solo números | El sistema busca coincidencias en cualquier parte del número local. |

> **Ejemplo:** La búsqueda `sw+%2B52` filtrará usuarios con la lada de México, mientras que `sw+555` buscará el patrón "555" al inicio pero sin considerar el prefijo internacional.

---

### 5. 🛡️ Seguridad y Manejo de Errores
*   **Protección de Credenciales:** Se implementó la **encriptación de contraseñas** antes de la persistencia. Nunca se almacena información sensible en texto plano en la base de datos.
*   **Manejo Global de Excepciones:** Mediante `@RestControllerAdvice`, se estandarizaron las respuestas de error (400, 404, 500) en formato JSON.
*   **Mensajes con Identidad:** Los errores no son genéricos; el sistema devuelve descripciones específicas (ej. errores de formato en RFC), facilitando la integración con el Front-End o clientes externos.

---

## 🔍 Endpoints Principales

| Método | Endpoint | Descripción |
| :--- | :--- | :--- |
| GET | `/users` | Listar todos los usuarios. |
| POST | `/users` | Crear un nuevo usuario. |
| GET | `/users/{id}` | Obtener detalle de un usuario específico. |
| PATCH | `/users/{id}` | Actualización parcial de un usuario (solo campos enviados). |
| DELETE | `/users/{id}` | Eliminar un usuario del sistema. |



## 📖 Documentación Interactiva (Swagger)
Una vez que la aplicación esté en ejecución, puedes acceder a la interfaz de Swagger para probar los endpoints:
![Vista de Swagger UI con los Endpoints](images/swagger.jpg)
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

> **Nota:** Se ha incluido el contrato de la API en la raíz del proyecto como `OpenApi.json` para facilitar su importación en herramientas como Postman.

---




## 🐳 Ejecución con Docker

El proyecto está orquestado para levantar tanto la base de datos MySQL como la API de forma automatizada. Asegúrate de tener Docker Desktop iniciado y sigue estos pasos en la terminal de tu preferencia:
1.  **Generar el ejecutable compilado:**
    Se omite la ejecución automatizada de pruebas en esta fase (`-DskipTests`) debido a que el contexto de Spring requiere que la conexión a la base de datos esté activa para verificar la persistencia. La base de datos real será inicializada por Docker en el paso posterior.
    ```bash
    ./mvnw clean package -DskipTests
    ```
2.  **Construir e iniciar los contenedores:**
    Este comando descargará la imagen de MySQL (si es la primera vez), construirá la imagen de la aplicación Java y levantará ambos servicios en segundo plano.
    ```bash
    docker compose up --build -d
    ```
3.  **Detener los servicios:**
    Cuando termines de utilizar la API, puedes apagar y limpiar los contenedores con el siguiente comando (los datos de los usuarios persistirán de forma segura en el volumen de Docker configurado):

    ```bash
    docker compose down
    ```

---

> **⚠️ Nota sobre el primer arranque (Cold Start):**
> La primera vez que se levantan los servicios, el motor de MySQL debe realizar configuraciones internas que pueden tomar entre 15 y 20 segundos. Si la aplicación de Spring Boot intenta conectarse antes de que la base de datos esté lista, el contenedor de la API podría detenerse.
> Si al intentar acceder a los endpoints recibes un error de conexión rechazada, simplemente reinicia el contenedor de la aplicación ejecutando:
> ```bash
> docker compose restart app
> ```
-----

## 📸 Evidencia de Funcionamiento 

Para validar el correcto funcionamiento de la API, se realizaron pruebas exhaustivas utilizando Postman. A continuación, se muestra una captura de pantalla de una petición exitosa:

![Prueba de API Exitosa en Postman](images/postman.jpg)

*Descripción: Validación de un endpoint (ej. Registro/Login) devolviendo un estatus **200 OK** y la respuesta en formato JSON, confirmando la correcta integración de la lógica de negocio, validaciones y encriptación.*

-----

## 🚀 Roadmap y Futuras Mejoras

Debido al tiempo limitado de la prueba técnica, se identificaron los siguientes puntos de mejora para una fase productiva:

*   **Seguridad y Gestión de Secretos:** Aunque actualmente se implementó la inyección de la `SECRET_KEY` mediante variables de entorno (archivo `.env`), la siguiente iteración consistiría en integrar un **Secrets Manager** (como AWS Secrets Manager, HashiCorp Vault o Azure Key Vault) para centralizar y rotar credenciales de forma dinámica en entornos cloud.
*   **Optimización de Persistencia:** Implementar **índices de base de datos** en campos de alta concurrencia de búsqueda y establecer **Reglas de Unicidad** a nivel base de datos para `email` y `teléfono`, reforzando la integridad que ya se valida en la capa de negocio.
*   **Pruebas Automatizadas:** Incrementar la cobertura de código mediante **Pruebas de Integración** con contenedores efímeros (usando Testcontainers) para validar el comportamiento real con MySQL antes de cada despliegue.
*   **Personalización por Negocio:** Adaptar reglas de validación específicas según el plan de negocio contratado (formatos de teléfono por país, etc.). La viabilidad técnica ya fue demostrada en las validaciones actuales; su expansión es una tarea puramente iterativa.

---


> **Observación Final:** Este proyecto fue diseñado bajo principios de **código limpio (Clean Code)** y escalabilidad, permitiendo que la lógica de negocio crezca sin comprometer la estabilidad de los servicios existentes.

