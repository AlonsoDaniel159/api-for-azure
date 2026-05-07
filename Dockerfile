FROM openjdk:21-slim

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado
COPY target/api-for-azure-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

