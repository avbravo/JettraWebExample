# Arquitectura de JettraWebExample

Este proyecto es una aplicación de demostración que integra el motor backend **JettraServer** con el framework de interfaces de usuario **JettraWUI**, ambos optimizados sobre **Java 25**.

## Patrón de Arquitectura

El proyecto está diseñado bajo un modelo híbrido embebido que aglutina configuración, backend y front-end web puramente con sintaxis Java:

1. **Frontend Abstracto (JettraWUI)**: En el método `initUI()`, los componentes de presentación (como paneles, canvas 3D futuristas y formularios *Glassmorphism*) son definidos enteramente en código Java mediante las clases base de la abstracción WUI. No requiere manejo directo de HTML ni React o Angular separados por defecto en este modelo.
   
2. **Servidor HTTP Embebido (JettraServer)**: JettraServer interviene levantando la instancia central (`JettraServer.main`) y enrutará internamente las peticiones HTTP y devolverá los recursos DOM, pre-renderizando el código Java abstracto como front-end estricto para el navegador.

3. **Inyección de Configuración Central**: Toda la aplicación usa la inyección basada en anotaciones de JettraServer (`@JettraConfigProperty`) extrayendo valores estructurados desde archivos externos estáticos o de variables de entorno de Docker (`jettra-config.properties`).
