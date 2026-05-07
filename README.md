# API REST de Productos

Una API REST simple pero funcional construida con **Spring Boot 4.0.6**, **H2 Database** y **Maven**. Perfecta para tu primer despliegue en AZURE.

## Características

✅ **CRUD Completo**: Crear, Leer, Actualizar y Eliminar productos  
✅ **Base de datos H2 en memoria**: Sin necesidad de configuraciones externas  
✅ **Validaciones**: Con anotaciones de Jakarta Validation  
✅ **Manejo de excepciones**: Respuestas de error consistentes  
✅ **CORS habilitado**: Para solicitudes desde diferentes orígenes  
✅ **H2 Console**: Interfaz web para explorar la base de datos  
✅ **Datos iniciales**: 6 productos precargados al iniciar  

## Requisitos

- **Java 21** o superior
- **Maven 3.6+**

## Instalación y Ejecución Local

### 1. Clonar o descargar el proyecto

```bash
cd C:\azure\projects\api-for-azure
```

### 2. Compilar el proyecto

```bash
mvn clean install -DskipTests
```

### 3. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Endpoints de la API

### 1. **Listar todos los productos**
```
GET /api/productos
```

**Respuesta:**
```json
[
  {
    "id": 1,
    "nombre": "Laptop Dell",
    "descripcion": "Laptop Dell XPS 15, procesador Intel i7, 16GB RAM",
    "precio": 1299.99,
    "cantidad": 5,
    "fechaCreacion": "2026-05-05T11:56:46.697502",
    "fechaActualizacion": "2026-05-05T11:56:46.697502"
  }
]
```

### 2. **Obtener un producto por ID**
```
GET /api/productos/{id}
```

**Ejemplo:**
```bash
curl -X GET http://localhost:8080/api/productos/1
```

### 3. **Buscar productos por nombre**
```
GET /api/productos/buscar/nombre?nombre=Mouse
```

**Respuesta:**
```json
[
  {
    "id": 2,
    "nombre": "Mouse Logitech",
    "descripcion": "Mouse inalambrico Logitech MX Master 3",
    "precio": 99.99,
    "cantidad": 15,
    "fechaCreacion": "2026-05-05T11:56:46.725503",
    "fechaActualizacion": "2026-05-05T11:56:46.725503"
  }
]
```

### 4. **Crear un nuevo producto**
```
POST /api/productos
Content-Type: application/json

{
  "nombre": "iPad Pro",
  "descripcion": "iPad Pro 12.9 pulgadas, 256GB",
  "precio": 1099.99,
  "cantidad": 7
}
```

**Respuesta (201 Created):**
```json
{
  "id": 7,
  "nombre": "iPad Pro",
  "descripcion": "iPad Pro 12.9 pulgadas, 256GB",
  "precio": 1099.99,
  "cantidad": 7,
  "fechaCreacion": "2026-05-05T12:00:00.123456",
  "fechaActualizacion": "2026-05-05T12:00:00.123456"
}
```

### 5. **Actualizar un producto**
```
PUT /api/productos/{id}
Content-Type: application/json

{
  "nombre": "iPad Pro Max",
  "descripcion": "iPad Pro 12.9 pulgadas, 512GB",
  "precio": 1299.99,
  "cantidad": 5
}
```

### 6. **Eliminar un producto**
```
DELETE /api/productos/{id}
```

**Respuesta:**
```json
{
  "mensaje": "Producto eliminado correctamente"
}
```

### 7. **Health Check**
```
GET /api/productos/health
```

**Respuesta:**
```json
{
  "status": "UP",
  "mensaje": "API de Productos está funcionando correctamente"
}
```

## H2 Console

Para acceder a la consola H2 y ver la base de datos:

1. La aplicación debe estar en ejecución
2. Abre en tu navegador: `http://localhost:8080/h2-console`
3. Usa estas credenciales:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **User Name**: `sa`
   - **Password**: (dejar en blanco)

## Estructura del Proyecto

```
api-for-azure/
├── src/main/java/com/alonso/firstapi/azure/
│   ├── ApiForAzureApplication.java          # Clase principal
│   ├── controller/
│   │   └── ProductoController.java        # REST endpoints
│   ├── service/
│   │   └── ProductoService.java           # Lógica de negocio
│   ├── repository/
│   │   └── ProductoRepository.java        # Acceso a datos
│   ├── entity/
│   │   └── Producto.java                  # Modelo de datos
│   ├── exception/
│   │   └── GlobalExceptionHandler.java    # Manejo de excepciones
│   └── config/
│       └── DataInitializer.java           # Datos iniciales
├── src/main/resources/
│   └── application.properties             # Configuración
├── pom.xml                                # Dependencias Maven
└── README.md                              # Este archivo
```

## Modelo de Datos - Producto

```java
{
  id              : Long          # ID único (auto-generado)
  nombre          : String        # Nombre del producto (requerido)
  descripcion     : String        # Descripción (opcional)
  precio          : BigDecimal    # Precio (mayor a 0)
  cantidad        : Integer       # Cantidad en stock (mayor a 0)
  fechaCreacion   : LocalDateTime # Fecha de creación
  fechaActualizacion : LocalDateTime # Última actualización
}
```

## Despliegue en AZURE

### Opción 1: Elastic Beanstalk (Recomendado)

1. **Empaquetar la aplicación:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **Crear un archivo `.ebextensions/maven.config`:**
   ```
   option_settings:
     azure:elasticbeanstalk:container:tomcat:jvmoptions:
       -Xmx256m: null
   ```

3. **Subir a Elastic Beanstalk:**
   ```bash
   eb create api-productos
   eb deploy
   ```

### Opción 2: EC2 + Docker

1. **Crear un Dockerfile:**
   ```dockerfile
   FROM openjdk:21-slim
   WORKDIR /app
   COPY target/api-for-azure-0.0.1-SNAPSHOT.jar app.jar
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

2. **Construir la imagen:**
   ```bash
   docker build -t api-productos .
   ```

3. **Subir a ECR y desplegar en EC2**

### Opción 3: RDS para Producción

Para usar una base de datos relacional en producción, actualiza `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://tu-rds-endpoint:3306/api_database
spring.datasource.username=admin
spring.datasource.password=tu_contraseña
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

Y añade la dependencia en `pom.xml`:

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>
```

## Ejemplos con cURL

```bash
# Listar todos los productos
curl -X GET http://localhost:8080/api/productos

# Obtener producto por ID
curl -X GET http://localhost:8080/api/productos/1

# Buscar por nombre
curl -X GET http://localhost:8080/api/productos/buscar/nombre?nombre=Dell

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Samsung Galaxy S24",
    "descripcion": "Smartphone Samsung Galaxy S24, 256GB",
    "precio": 899.99,
    "cantidad": 20
  }'

# Actualizar producto
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop Dell XPS 13",
    "precio": 1199.99,
    "cantidad": 3
  }'

# Eliminar producto
curl -X DELETE http://localhost:8080/api/productos/1

# Health check
curl -X GET http://localhost:8080/api/productos/health
```

## Errores Comunes

### Error: `Port 8080 is already in use`
**Solución:** Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### Error: `Table PRODUCTOS not found`
**Solución:** Asegúrate de que Hibernate cree las tablas con `create-drop` o `update` en `application.properties`.

## Mejoras Futuras

- [ ] Autenticación y autorización (JWT)
- [ ] Paginación en listados
- [ ] Filtros avanzados
- [ ] Logging mejorado
- [ ] Tests unitarios e integración
- [ ] API Documentation con Swagger/SpringFox
- [ ] Caché con Redis
- [ ] Escalabilidad horizontal

## Licencia

Este proyecto es de código abierto y puede ser usado libremente.

## Autor

Creado por GitHub Copilot

## Soporte

Para reportar issues o sugerencias, por favor abre un issue en el repositorio.

---

**¡Listo para desplegar en AZURE! 🚀**

