# 🚀 GUÍA RÁPIDA - API REST Productos

## ✅ Tu API ya está LISTA y FUNCIONANDO!

La aplicación se está ejecutando en: **http://localhost:8080**

---

## 📦 ¿Qué se creó?

Una API REST **completa y funcional** con:

✅ **CRUD Completo** (Crear, Leer, Actualizar, Eliminar)  
✅ **Base de datos H2 en memoria**  
✅ **6 productos precargados**  
✅ **Validaciones de entrada**  
✅ **Manejo de excepciones**  
✅ **CORS habilitado**  
✅ **H2 Console para explorar datos**  
✅ **Documentación completa**  

---

## 🧪 Prueba Rápida de los Endpoints

### 1. Ver todos los productos
```bash
curl http://localhost:8080/api/productos
```

### 2. Ver un producto específico
```bash
curl http://localhost:8080/api/productos/1
```

### 3. Crear un producto
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Samsung TV","descripcion":"Tv 55 pulgadas","precio":599.99,"cantidad":5}'
```

### 4. Actualizar un producto
```bash
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Dell XPS 13","precio":1299.99}'
```

### 5. Eliminar un producto
```bash
curl -X DELETE http://localhost:8080/api/productos/7
```

### 6. Buscar por nombre
```bash
curl "http://localhost:8080/api/productos/buscar/nombre?nombre=Dell"
```

### 7. Health Check
```bash
curl http://localhost:8080/api/productos/health
```

---

## 🗄️ Ver la Base de Datos (H2 Console)

1. Abre en tu navegador: **http://localhost:8080/h2-console**
2. Usa:
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **Username**: `sa`
   - **Password**: (dejar en blanco)

---

## 📁 Estructura del Proyecto

```
api-for-azure/
├── src/main/java/com/alonso/firstapi/azure/
│   ├── ApiForAzureApplication.java          ← Clase Principal
│   ├── controller/ProductoController.java ← REST Endpoints
│   ├── service/ProductoService.java       ← Lógica de Negocio
│   ├── repository/ProductoRepository.java ← Acceso a BD
│   ├── entity/Producto.java               ← Modelo de Datos
│   ├── exception/GlobalExceptionHandler.java ← Manejo de Errores
│   └── config/DataInitializer.java        ← Datos Iniciales
├── pom.xml                                ← Dependencias
├── README.md                              ← Documentación Completa
├── DEPLOYMENT.md                          ← Guía de Despliegue AZURE
└── Dockerfile                             ← Para desplegar en Docker
```

---

## 📊 Información de la Aplicación

| Componente | Detalles |
|-----------|----------|
| **Framework** | Spring Boot 4.0.6 |
| **Java** | Versión 21 |
| **Base de Datos** | H2 (en memoria) |
| **Build Tool** | Maven |
| **Puerto** | 8080 |
| **URL Base** | http://localhost:8080/api/productos |

---

## 🔧 Comandos Importantes

### Compilar el proyecto
```bash
mvn clean install -DskipTests
```

### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

### Empaquetar para producción
```bash
mvn clean package -DskipTests
```

### Ejecutar con Docker
```bash
docker build -t api-productos .
docker run -p 8080:8080 api-productos
```

---

## 🌐 Despliegue en AZURE

Tenemos 3 opciones principales:

### Opción 1: Elastic Beanstalk (Recomendado) ⭐
```bash
eb init -p java-21 api-productos
eb create prod-env
eb deploy
```

### Opción 2: EC2 + Docker + ECR
```bash
# Construir imagen
docker build -t api-productos .
# Subir a ECR
docker tag api-productos:latest 123456789.dkr.ecr.us-east-1.amazonazure.com/api-productos
docker push 123456789.dkr.ecr.us-east-1.amazonazure.com/api-productos
```

### Opción 3: EC2 Simple
```bash
# Copiar JAR a EC2 y ejecutar
java -jar api-for-azure-0.0.1-SNAPSHOT.jar
```

**Para más detalles, lee: `DEPLOYMENT.md`**

---

## 🧪 Endpoints Disponibles

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos |
| GET | `/api/productos/{id}` | Obtener por ID |
| GET | `/api/productos/buscar/nombre?nombre=X` | Buscar por nombre |
| POST | `/api/productos` | Crear nuevo |
| PUT | `/api/productos/{id}` | Actualizar |
| DELETE | `/api/productos/{id}` | Eliminar |
| GET | `/api/productos/health` | Health Check |
| GET | `/h2-console` | Consola H2 |

---

## 📝 Esquema de Producto

```json
{
  "id": 1,
  "nombre": "String (requerido)",
  "descripcion": "String (opcional)",
  "precio": 99.99,
  "cantidad": 10,
  "fechaCreacion": "2026-05-05T11:56:46.697502",
  "fechaActualizacion": "2026-05-05T11:56:46.697502"
}
```

---

## ✨ Características Destacadas

- **Validación automática** con Jakarta Validation
- **Timestamps automáticos** (creación y actualización)
- **Control de transacciones** con @Transactional
- **Inyección de dependencias** con Lombok
- **CORS para desarrollo** (acceso desde cualquier origen)
- **Respuestas JSON** consistentes
- **Manejo de errores** centralizado

---

## 🔍 Ejemplos de Respuestas

### ✅ Respuesta Exitosa (200 OK)
```json
{
  "id": 1,
  "nombre": "Laptop Dell",
  "precio": 1299.99,
  "cantidad": 5
}
```

### ❌ Error de Validación (400 Bad Request)
```json
{
  "status": 400,
  "mensaje": "Errores de validación",
  "errores": {
    "nombre": "El nombre del producto es requerido",
    "precio": "El precio debe ser mayor a 0"
  }
}
```

### ❌ Recurso No Encontrado (404)
```json
{
  "status": 404,
  "mensaje": "Producto no encontrado con ID: 999"
}
```

---

## 🚨 Solución de Problemas

### Puerto 8080 en uso
```bash
# Cambiar puerto en application.properties
server.port=8081
```

### Tabla no encontrada
```bash
# Verificar que Hibernate cree las tablas
spring.jpa.hibernate.ddl-auto=create-drop
```

### Errores de compilación
```bash
mvn clean install -X
```

---

## 📚 Archivos Principales

| Archivo | Propósito |
|---------|-----------|
| `README.md` | Documentación completa de la API |
| `DEPLOYMENT.md` | Guía de despliegue en AZURE |
| `pom.xml` | Dependencias del proyecto |
| `Dockerfile` | Para despliegue con Docker |
| `application.properties` | Configuración de desarrollo |

---

## 🎯 Próximos Pasos

1. **Prueba local**: Verifica que todo funcione (`curl` o Postman)
2. **Lee la documentación**: `README.md` y `DEPLOYMENT.md`
3. **Configura AZURE**: Crea cuenta y credenciales
4. **Deploya**: Sigue las instrucciones en `DEPLOYMENT.md`
5. **Dale mantenimiento**: Actualiza según tus necesidades

---

## 📞 Ayuda y Recursos

- **Documentación oficial**: https://spring.io/projects/spring-boot
- **Spring Boot Starters**: https://spring.io/guides/gs/spring-boot/
- **AZURE Elastic Beanstalk**: https://docs.azure.amazon.com/elasticbeanstalk/
- **H2 Database**: http://www.h2database.com/

---

## ✅ Checklist antes de Desplegar

- [ ] Probé todos los endpoints localmente
- [ ] Compilé el proyecto sin errores (`mvn clean install`)
- [ ] Generé el JAR ejecutable
- [ ] Leí DEPLOYMENT.md
- [ ] Tengo cuenta en AZURE
- [ ] Configuré credenciales de AZURE
- [ ] Probé en AZURE en ambiente de prueba
- [ ] Mi aplicación funciona en `http://localhost:8080`

---

## 🎉 ¡LISTO!

Tu API REST está **completamente funcional** y lista para desplegar en AZURE.

**¡A desplegar! 🚀**

---

*Última actualización: 2026-05-05*

