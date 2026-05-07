FROM eclipse-temurin:21-jdk

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el JAR compilado
COPY target/*.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]

