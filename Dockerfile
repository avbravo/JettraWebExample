# Empleando una imagen ligera orientada a un runtime óptimo y moderno
# Usando BellSoft Liberica JRE con Alpaquita Linux para menor tamaño y mayor rendimiento
FROM bellsoft/liberica-runtime-container:jre-25-alpaquita

LABEL maintainer="Jettra Development Team"
LABEL description="Contenedor de Demostración JettraWeb Example optimizado con Alpaquita"

WORKDIR /app

# Copia el JAR compilado desde target de su entorno en Host
COPY target/JettraWebExample-1.0-SNAPSHOT.jar /app/JettraWebExample.jar

# Exponer el puerto por defecto (configurable via -e variables de entorno o property config)
EXPOSE 8080

# Opciones de JVM para optimización de rendimiento en contenedores
ENV JAVA_OPTS="-XX:+UseZGC -XX:+ZGenerational -Xmx512m -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar JettraWebExample.jar"]
