# Empleando una imagen ligera orientada a un runtime óptimo y moderna iteración LTS ó soporte Java 25 minimalista
FROM openjdk:25-jdk-slim

LABEL maintainer="Jettra Development Team"
LABEL description="Contenedor de Demostración JettraWeb Example"

WORKDIR /app

# Copia el JAR compilado desde target de su entorno en Host
COPY target/JettraWebExample-1.0-SNAPSHOT.jar /app/JettraWebExample.jar

# Exponer el puerto por defecto (configurable via -e variables de entorno o property config)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "JettraWebExample.jar"]
