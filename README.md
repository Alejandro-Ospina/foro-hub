# Foro Hub Alura

Foro Hub Alura es una API desarrollada como parte de un reto formativo proporcionado por Alura Latam y Oracle. Este proyecto implementa un sistema de foros que permite a los usuarios interactuar en temas, responder publicaciones y gestionar recursos de manera segura mediante autenticación JWT.

## Índice de Contenidos

1. [Tecnologías Usadas](#tecnologias-usadas)
2. [Configuración del Proyecto](#configuracion-del-proyecto)
3. [Configuración de Variables de Entorno](#configuracion-de-variables-de-entorno)
4. [Configuración de la Base de Datos PostgreSQL](#configuracion-de-la-base-de-datos-postgresql)
5. [Dependencias Necesarias](#dependencias-necesarias)
6. [Autenticación JWT](#autenticacion-jwt)
   - [Flujo de Autenticación](#flujo-de-autenticacion)
   - [Filtro de Autenticación e Interceptores](#filtro-de-autenticacion-e-interceptores)
7. [Ejecución del JAR en Local](#ejecucion-del-jar-en-local)
8. [Agradecimientos](#agradecimientos)

---

## Tecnologías Usadas

El proyecto utiliza una serie de herramientas y tecnologías modernas para garantizar un desarrollo robusto y escalable:

- **Java 17**: Lenguaje base que proporciona capacidades modernas, mayor seguridad y mejoras en el rendimiento.
- **Spring Boot (v3.4.1)**: Framework para simplificar la configuración y el desarrollo de aplicaciones Java.
- **Spring Security**: Manejador de seguridad que permite autenticar y autorizar solicitudes con facilidad.
- **Hibernate (JPA)**: Herramienta ORM para interactuar con la base de datos de forma eficiente.
- **PostgreSQL**: Sistema de gestión de bases de datos relacional utilizado como base de datos principal.
- **SpringDoc OpenAPI**: Generador de documentación interactiva para la API con Swagger UI.
- **Lombok**: Biblioteca que reduce el código repetitivo al generar automáticamente métodos como getters, setters, constructores y más mediante anotaciones.
- **MapStruct 1.6.2**: Framework de mapeo entre objetos que permite convertir entidades en DTOs y viceversa, siguiendo principios de diseño limpio y extensibilidad.

---

## Configuración del Proyecto

1. Clona el repositorio:
   ```bash
   git clone <repositorio>
   cd foro-hub
   ```
2. Asegúrate de tener configuradas las variables de entorno y la base de datos PostgreSQL correctamente.
3. Compila y ejecuta el proyecto con Maven:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

---

## Configuración de Variables de Entorno

El proyecto requiere variables de entorno configuradas para conectarse a la base de datos y gestionar JWT. Configura las siguientes variables:

```bash
# Base de datos
DB_URL=jdbc:postgresql://dominio_o_direccion_ip:puerto/nombre_base_de_datos
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_password

# JWT
SECRET=clave_secreta
```

Estas variables pueden configurarse en el archivo `application.properties` para desarrollo local:

```properties
## Configuración base de datos
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

## Configuración clave secreta para firma de jwt
jwt.secret.key=${SECRET}
```

---

## Configuración de la Base de Datos PostgreSQL

1. Crea una base de datos llamada `foro_hub`:
   ```sql
   CREATE DATABASE foro_hub;
   ```
2. Asegúrate de que las credenciales configuradas en las variables de entorno coincidan con el usuario y contraseña de PostgreSQL.
3. El esquema de la base de datos se generará automáticamente al iniciar la aplicación si tienes configurado `spring.jpa.hibernate.ddl-auto=update`.

---

## Dependencias Necesarias

Las dependencias clave para el funcionamiento del proyecto están definidas en el archivo `pom.xml`:

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
  <version>11.1.0</version>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-database-postgresql</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
<dependency>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct</artifactId>
  <version>1.6.2</version>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-test</artifactId>
  <scope>test</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-api</artifactId>
  <version>0.12.6</version>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-impl</artifactId>
  <version>0.12.6</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>io.jsonwebtoken</groupId>
  <artifactId>jjwt-jackson</artifactId> <!-- or jjwt-gson if Gson is preferred -->
  <version>0.12.6</version>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
  <version>2.7.0</version>
</dependency>
<dependency>
  <groupId>org.springdoc</groupId>
  <artifactId>springdoc-openapi-starter-webmvc-api</artifactId>
  <version>2.7.0</version>
</dependency>
  ```

- **MapStruct**: Debes verificar en el procesador de anotaciones del pom, que el path hacia el procesador de la versión de mapstruct se incluya.

```xml
<path>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct-processor</artifactId>
  <version>1.6.2</version>
</path>
```

---

## Autenticación JWT

### Flujo de Autenticación
El flujo de autenticación JWT sigue los siguientes pasos:

1. **Registro**: Los usuarios pueden registrarse en el endpoint:
   ```http
   POST /usuarios/registrar
   {
      "nombre": "nombre",
      "email": "correo",
      "pass": "pass",
      "perfiles": [
          {
              "nombre": "perfil1"
          },
          {
              "nombre": "perfil2"
          },
          {
              "nombre": "perfil3"
          }
       ]
    }
   ```
2. **Inicio de Sesión**: Al iniciar sesión, se genera un token JWT:
   ```http
   POST /usuarios/login
   {
       "email": "correo",
       "password": "pass"
   }
   ```
3. **Autorización**: Todas las solicitudes protegidas requieren el token en el encabezado `Authorization`:
   ```http
   Authorization: Bearer <token>
   ```

### Filtro de Autenticación e Interceptores

El filtro de autenticación verifica el token JWT y determina si el usuario tiene permisos. Además, los interceptores validan si el usuario es propietario del recurso solicitado.

**Filtro de Autenticación:**
```java
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        var header = request.getHeader("Authorization");
        if (header != null){

            var token = header.replace("Bearer ", "");
            if (!jwtService.tokenValido(token)){
                // Se devuelve una respuesta formateada a json que le informa al usuario sobre el token inválido
            }

            // Se obtiene el sujeto del token, que en este caso es el correo electrónico.
            // Nota: usted puede configurar como sujeto el nombre del usuario u otro parámetro que crea adecuado

            var email = jwtService.obtenerSujeto(token);
            var usuario = usuarioRepository.findByEmail(email).orElse(null);
            if (usuario != null && !usuario.getActivo()){
                // Si existe el usuario, pero este no está activo, se devuelve una respuesta informándole al usuario
                // que no está activo, y por ende, no tendrá acceso a los recursos
            }
              
            // Se crea un contexto de seguridad que carga de nuevo el usuario, y se sigue la solicitud

        filterChain.doFilter(servletRequest, servletResponse);
    }
```

**Validación de Propietario:**
```java
if (!recurso.getPropietario().equals(usuarioAutenticado)) {
    throw new AccessDeniedException("No tiene permisos para editar este recurso.");
}
```

---

## Ejecución del JAR en Local

1. Compila el proyecto:
   ```bash
   mvn clean package
   ```
2. Asegúrate de que las variables de entorno y la base de datos estén configuradas.
3. Ejecuta el JAR generado:
   ```bash
   java -jar target/foro-hub-1.0.0.jar
   ```

---

## Agradecimientos

Agradezco a **Alura Latam** y **Oracle** por el reto formativo que permite a desarrolladores fortalecer sus habilidades y crecer profesionalmente. Este proyecto es un testimonio del impacto positivo que tienen estos desafíos en el desarrollo de software.


