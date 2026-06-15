# Guía de Seguridad con JWT en Jettra Web

Esta guía explica cómo se ha implementado la seguridad con JSON Web Tokens (JWT) en los microservicios REST generados en **JettraServer**, y cómo la interfaz gráfica (**Jettra Web UI**) interactúa con ellos de forma segura autenticándose en nombre del usuario que ha iniciado sesión.

## 1. Protección de Microservicios con `@Secured`

Para asegurar que un endpoint sólo pueda ser consumido por clientes autenticados, Jettra incorpora la anotación `@Secured`.

### Ejemplo Práctico (`AuthorController`)

Simplemente debes añadir `@Secured` a nivel de clase o de método en tu controlador REST. Cuando `JettraServer` reciba una solicitud a esta ruta, interceptará la petición y validará que la cabecera contenga un token **Bearer** válido.

```java
import com.jettra.rest.annotations.*;

@Path("/api/library/authors")
@Secured // <- Asegura que todos los endpoints requieran JWT
public class AuthorController {
    
    @GET
    public List<AuthorModel> findAll() {
        return AuthorRepository.findAll();
    }
}
```

## 2. Consumo de Microservicios Protegidos

Los microservicios son consumidos internamente mediante Rest Clients en la WUI. Para enviar el token, debemos modificar la interfaz del cliente agregando el `HeaderParam`.

### Modificación del Cliente REST (`IAuthorRestClient`)

```java
import com.jettra.rest.annotations.HeaderParam;

public interface IAuthorRestClient {
    // Es necesario indicar que todos los métodos recibirán un token
    List<AuthorModel> findAll(@HeaderParam("Authorization") String token);
}
```

### Generación del Token en el Servicio (`AuthorService`)

Para mantener el flujo de seguridad, el servicio que interactúa entre la Web UI y el Rest Client genera de forma dinámica el JWT tomando el nombre de usuario del `JettraContext`. 

El `JettraContext` guarda los datos de la sesión del usuario cuando éste hace login en el `LoginPage`.

```java
import com.jettra.server.core.JettraContext;
import com.jettra.jwt.JettraJWT;

public class AuthorService {

    // 1. Extraemos el usuario actual de la sesión
    // 2. Generamos el JWT utilizando la misma llave secreta configurada en el servidor
    private static String getJwtToken() {
        String user = "anonymous";
        
        if (JettraContext.getCurrent() != null) {
            Object sessionUser = JettraContext.getCurrent().get(JettraContext.Scope.SESSION, "username");
            if (sessionUser != null) {
                user = sessionUser.toString();
            }
        }
        
        // Llave y tiempo de expiración (1 hora)
        JettraJWT jwt = new JettraJWT("default_secret_key_jettra_rest_2026", 3600000);
        return "Bearer " + jwt.generateToken(new HashMap<>(), user);
    }

    // El servicio utiliza el token al hacer la petición
    public static List<AuthorModel> findAll() {
        return client.findAll(getJwtToken());
    }
}
```

## 3. Registro de Sesión en la Aplicación Web

Para que el proceso anterior funcione, el `LoginPage` de la aplicación web debe encargarse de almacenar al usuario en el `JettraContext` una vez que la autenticación es exitosa.

```java
// En LoginPage.java (onPost)
if (isValidUser(user, pass)) {
    // Guardar cookie tradicional para la UI
    setSessionCookie(user, cPath);
    
    // Registrar la sesión en el servidor para que los servicios estáticos puedan acceder
    JettraContext.getCurrent().set(JettraContext.Scope.SESSION, "username", user);
    
    // Redirigir
    redirect(currentExchange, JettraServer.resolvePath("/dashboard"));
}
```

## 4. Probando la API Interactiva (Swagger UI)

`JettraServer` inyecta automáticamente el esquema de **Bearer Auth** a tu documentación de Swagger UI. Esto te permite probar todos tus endpoints protegidos de forma interactiva.

1. Navega a `http://localhost:8080/swagger-ui`.
2. Haz clic en el botón verde **Authorize** situado en la parte superior.
3. Ingresa tu JWT (que debe empezar con `Bearer ...` o simplemente el token, dependiendo de cómo genere Swagger el header final; por defecto en Jettra solo necesitas pegar el token).
4. Todas las peticiones posteriores incluirán este token, permitiendo pruebas integradas y rápidas.

## 5. Ejemplos de Pruebas Manuales (cURL)

Si deseas consumir el servicio REST fuera de la UI de Jettra o Swagger (por ejemplo desde una aplicación móvil u otro servidor), puedes enviar el token de la siguiente forma:

### 5.1. Generar un JWT válido desde consola

Para generar un JWT y obtener el token por consola de una manera reutilizable y cómoda, puedes compilar y ejecutar una clase utilitaria apoyándote en Maven.

Crea un archivo en tu proyecto llamado `GenerateToken.java` (por ejemplo, en el paquete `com.jettra.example.util`):

```java
// GenerateToken.java
package com.jettra.example.util;

import java.util.HashMap;
import java.util.Scanner;
import com.jettra.jwt.JettraJWT;

public class GenerateToken {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Ingrese username: ");
        String username = scanner.nextLine();
        
        System.out.print("Ingrese password: ");
        String password = scanner.nextLine();
        
        // Aquí podrías validar contra una base de datos o lógica real.
        // Para el ejemplo, generamos el JWT si envía información válida.
        if (username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Error: Credenciales inválidas.");
            System.exit(1);
        }

        String secret = "default_secret_key_jettra_rest_2026";
        long expiration = 3600000; // 1 hora

        JettraJWT jwt = new JettraJWT(secret, expiration);
        String token = jwt.generateToken(new HashMap<>(), username);

        System.out.println("\n[JWT Generado Exitosamente]");
        System.out.println("Bearer " + token);
    }
}
```

Compílalo y ejecútalo (asegúrate de incluir las dependencias de Jettra en el `-cp`):
```bash
# Ejemplo si usas Maven exec:java para correr un main temporal
mvn exec:java -Dexec.mainClass="com.jettra.example.util.GenerateToken"
```

### 5.2. Consumir la API protegida

Una vez obtenido el token que empieza por `Bearer ...`:

```bash
# Exportar el token 
export TOKEN="Bearer eyJhbGciOiJIUz..."

# Hacer la petición pasando el header
curl -X GET "http://localhost:8080/jettrawebexample/api/library/authors" \
     -H "Authorization: $TOKEN" \
     -H "Accept: application/json"
```
