# Usamos una imagen base de Java 17
FROM eclipse-temurin:17-jdk-alpine

# creamos un directorio para la app
WORKDIR /app

# Copiamos el JAR desde la carpeta target al contenedor
COPY target/Prueba_Tecnica-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto 8080
EXPOSE 8080

# ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]