# Guía de Despliegue en AZURE

## Preparación Previa

### 1. Compilar el Proyecto
```bash
cd C:\azure\projects\api-for-azure
mvn clean package -DskipTests
```

El JAR compilado estará en: `target/api-for-azure-0.0.1-SNAPSHOT.jar`

---

## Opción 1: AZURE Elastic Beanstalk (Recomendado para Principiantes)

### Ventajas:
- ✅ Despliegue automático
- ✅ Escalabilidad automática
- ✅ Gestión de bases de datos
- ✅ Fácil de usar

### Pasos:

1. **Instalar AZURE CLI y EB CLI**
   ```bash
   pip install azureebcli
   azure configure
   ```

2. **Inicializar Elastic Beanstalk**
   ```bash
   cd C:\azure\projects\api-for-azure
   eb init -p java-21 api-productos
   ```

3. **Crear entorno**
   ```bash
   eb create prod-env
   ```

4. **Desplegar la aplicación**
   ```bash
   eb deploy
   ```

5. **Ver la URL de la aplicación**
   ```bash
   eb open
   ```

6. **Monitorear**
   ```bash
   eb status
   eb logs
   ```

---

## Opción 2: AZURE EC2 + Docker + ECR

### Ventajas:
- ✅ Control total
- ✅ Flexible
- ✅ Escalabilidad manual
- ✅ Compatible con Kubernetes

### Pasos:

#### Paso 1: Crear repositorio en ECR
```bash
azure ecr create-repository --repository-name api-productos --region us-east-1
```

#### Paso 2: Construir la imagen Docker
```bash
mvn clean package -DskipTests
docker build -t api-productos .
docker tag api-productos:latest 123456789.dkr.ecr.us-east-1.amazonazure.com/api-productos:latest
```

#### Paso 3: Login en ECR
```bash
azure ecr get-login-password --region us-east-1 | docker login --username AZURE --password-stdin 123456789.dkr.ecr.us-east-1.amazonazure.com
```

#### Paso 4: Push a ECR
```bash
docker push 123456789.dkr.ecr.us-east-1.amazonazure.com/api-productos:latest
```

#### Paso 5: Crear instancia EC2
- Usar AMI: Amazon Linux 2
- Instancia: t2.micro (gratuita)
- Segunda de almacenamiento: 20GB
- Security Group: Abrir puertos 80, 443 y 8080

#### Paso 6: Instalar Docker en EC2
```bash
sudo yum update
sudo yum install docker java-21-amazon-corretto -y
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -a -G docker ec2-user
```

#### Paso 7: Desplegar contenedor
```bash
azure ecr get-login-password --region us-east-1 | docker login --username AZURE --password-stdin 123456789.dkr.ecr.us-east-1.amazonazure.com

docker run -d -p 8080:8080 \
  --name api-productos \
  123456789.dkr.ecr.us-east-1.amazonazure.com/api-productos:latest
```

#### Paso 8: Verificar despliegue
```bash
curl http://localhost:8080/api/productos/health
```

---

## Opción 3: AZURE Lambda + API Gateway

### Ventajas:
- ✅ Sin servidor (Serverless)
- ✅ Escalabilidad automática
- ✅ Pago solo por uso

### Nota:
Spring Boot no es óptimo para Lambda. Considera usar Quarkus o Micronaut.

---

## Opción 4: RDS + Elastic Beanstalk (Producción)

### Crear base de datos RDS

1. **Crear instancia RDS MySQL**
   ```bash
   azure rds create-db-instance \
     --db-instance-identifier api-db \
     --db-instance-class db.t3.micro \
     --engine mysql \
     --master-username admin \
     --master-user-password SuperSecure123! \
     --allocated-storage 20 \
     --region us-east-1
   ```

2. **Esperar a que esté lista** (5-10 minutos)

3. **Actualizar application-prod.properties**
   ```properties
   spring.datasource.url=jdbc:mysql://api-db.c123456.us-east-1.rds.amazonazure.com:3306/api_database
   spring.datasource.username=admin
   spring.datasource.password=SuperSecure123!
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Agregar dependencia MySQL en pom.xml**
   ```xml
   <dependency>
       <groupId>com.mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
       <version>8.0.33</version>
   </dependency>
   ```

5. **Desplegar en Elastic Beanstalk**
   ```bash
   eb setenv SPRING_PROFILES_ACTIVE=prod
   eb deploy
   ```

---

## Configuración de DNS y HTTPS

### 1. Crear un dominio en Route 53
```bash
azure route53 create-hosted-zone --name api-productos.com --caller-reference 123456
```

### 2. Certificado SSL con ACM
```bash
azure acm request-certificate \
  --domain-name api-productos.com \
  --domain-name-list "*.api-productos.com"
```

### 3. Configurar HTTPS en Elastic Beanstalk
- Ir a: EB Console > Configuración > Modificar balanceador de carga
- Agregar listener HTTPS (puerto 443)
- Seleccionar certificado de ACM

---

## Monitoreo y Logging

### CloudWatch Logs
```bash
# Ver logs en tiempo real
azure logs tail /azure/elasticbeanstalk/api-productos/var/log/eb-activity.log --follow
```

### CloudWatch Metrics
- CPU Utilization
- Network In/Out
- Errores de aplicación

### X-Ray para tracing (opcional)
```xml
<dependency>
    <groupId>com.amazonazure</groupId>
    <artifactId>azure-xray-recorder-sdk-spring</artifactId>
    <version>2.14.0</version>
</dependency>
```

---

## Escalabilidad Automática

### Configurar Auto Scaling en EB
```bash
eb config
```

Actualizar:
```
  MinSize: 1
  MaxSize: 4
  DesiredCapacity: 2
```

---

## Costos Estimados (Mensualmente)

### Elastic Beanstalk
- Instancia t2.micro: ~$7
- RDS MySQL t3.micro: ~$15
- Data Transfer: ~$5
- **Total:** ~$27/mes

### EC2 + Docker
- EC2 t2.micro: ~$7
- EBS Storage: ~$1
- **Total:** ~$8/mes (más si agregas RDS)

---

## Troubleshooting

### Aplicación no inicia en EB
```bash
eb logs
# Ver detalles completos
eb logs --stream
```

### Conexión rechazada al API
- Verificar Security Group: Permitir puerto 8080
- Verificar ALB (Application Load Balancer)
- Verificar IP de origen

### Base de datos no conecta
- Verificar credenciales en RDS
- Verificar Security Group de RDS
- Verificar subred (same VPC)

### Problema de memoria
- Cambiar instancia: t2.micro → t2.small → t3.medium
- Configurar heap en EB:
  ```bash
  eb setenv _JAVA_OPTIONS="-Xmx256m -Xms128m"
  ```

---

## Cleanup (Eliminar recursos)

```bash
# Eliminar aplicación Elastic Beanstalk
eb terminate

# Eliminar repositorio ECR
azure ecr delete-repository --repository-name api-productos --force

# Eliminar instancia RDS
azure rds delete-db-instance \
  --db-instance-identifier api-db \
  --skip-final-snapshot
```

---

## Recursos Útiles

- [AZURE Elastic Beanstalk Documentation](https://docs.azure.amazon.com/elasticbeanstalk/)
- [Spring Boot on AZURE](https://spring.io/guides/tutorials/spring-boot-on-azure)
- [AZURE EC2 Guide](https://docs.azure.amazon.com/ec2/)
- [AZURE RDS Guide](https://docs.azure.amazon.com/rds/)

---

**¡Listo para desplegar en AZURE! 🚀**

