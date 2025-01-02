# Foro Hub Alura

Foro Hub Alura es una API desarrollada como parte de un reto formativo proporcionado por Alura Latam y Oracle. Este proyecto implementa un sistema de foros que permite a los usuarios interactuar en temas, responder publicaciones y gestionar recursos de manera segura mediante autenticación JWT.

## Índice de Contenidos

1. [Tecnologías Usadas](#tecnologias-usadas)
2. [Configuración del Proyecto](#configuracion-del-proyecto)
3. [Configuración de Variables de Entorno](#configuracion-de-variables-de-entorno)
4. [Configuración de la Base de Datos PostgreSQL](#configuracion-de-la-base-de-datos-postgresql)
5. [Dependencias Necesarias](#dependencias-necesarias)
6. [Sección de Usuarios](#seccion-de-usuarios)
7. [Sección de Cursos](#seccion-de-cursos)
8. [Sección de Tópicos](#seccion-de-topicos)
9. [Sección de Respuestas](#seccion-de-respuestas)
10. [Ejecución del JAR en Local](#ejecucion-del-jar-en-local)
11. [Agradecimientos](#agradecimientos)

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

**Detalles de las tecnologías:**
- **Lombok**: Permite simplificar el código mediante anotaciones como `@Data`, `@Builder`, `@Getter`, etc. Por ejemplo:

    ```java
    @Data
    @Builder
    public class Usuario {
        private Long id;
        private String nombre;
        private String email;
    }
    ```

    Esto elimina la necesidad de escribir manualmente los métodos `getters`, `setters`, y constructores.

- **MapStruct**: Facilita el mapeo de datos entre entidades y DTOs mediante interfaces. Por ejemplo, un mapeador para convertir un `Usuario` a un `UsuarioDTO`:

    ```java
    @Mapper
    public interface UsuarioMapper {
        UsuarioDTO toDTO(Usuario usuario);
        Usuario toEntity(UsuarioDTO usuarioDTO);
    }
    ```

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
4. Si deseas llevar el control de tu base de datos, usa un esquema de migración que te permita llevar el historial y versionado de tu DB (como flyway en este caso)

---

## Dependencias Necesarias

Las dependencias clave para el funcionamiento del proyecto están definidas en el archivo `pom.xml`. A continuación, se clasifican las dependencias usadas y su propósito;

1. **Dependencias de inicio**

Estas dependencias vienen por defecto en el paquete principal de spring web. Con ellas, viene adjunto un servidor tomcat embebido para ejecutar un archivo jar en cualquier servidor web, y una capa para ejcutar tests unitarios y de integración.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-test</artifactId>
  <scope>test</scope>
</dependency>
```

2. **Dependencias de base de datos**

Debido a que el gestor de base de datos elegido es postgre, se debe incluir el driver del gestor y el proveedor de mapeo objeto-relacional, que en este caso es Hibernate por medio de la dependencia Spring Data JPA.

```xml
<dependency>
  <groupId>org.postgresql</groupId>
  <artifactId>postgresql</artifactId>
  <scope>runtime</scope>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

3. **Dependencias de control de versiones para bases de datos**

Al anexar las dependencias de base de datos, spring nos solicitará que creemos una DB, sino, nos dará error de ejecución al momento de correr la aplicación. Por lo anterior, se usa **Flyway** para llevar a cabo el control de versiones de nuestra base de datos, y de esa forma. construir la versión 1 de nuestra DB para que Spring pueda establecer una conexión, y crear las entidades necesarias de nuestro proyecto.

```xml
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
  <version>11.1.0</version>
</dependency>
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-database-postgresql</artifactId>
</dependency>
```

**NOTA:** Recuerda que flyway es una herramienta de versionado de base de datos, por lo cual puedes modificar a tu criterio la base de datos creada. No olvides crear un archivo nuevo de migración cada vez que decidas hacer modificaciones.

4. **Dependencias para autenticación con JWT**

Para este proyecto, se usa la libreria ```jjwt```, Dando click en este [enlace](https://github.com/jwtk/jjwt) podrás acceder a la documentación oficial.

```xml
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
```

5. **Dependencias de validación de campos**

Spring nos ofrece una libreria importante para validar campos de una solicitud http, y esta es Spring validation. 

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

6. **Dependencia de seguridad**

Las herramientas de seguridad en cualquier aplicación web son imprescindibles, pues nos dotan de una amplia gama de librerias para gestionar el control de acceso a recursos. Con Spring security tendremos acceso a toda la gama de librerías de seguridad, y el estándar para construcción de tests unitarios para pruebas de seguridad.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-test</artifactId>
  <scope>test</scope>
</dependency>
```

7. **Dependencia de mapeo entidades-dtos**

Para el mapeo de objetos se usa Mapstruct en su versión 1.6.2.

```xml
<dependency>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct</artifactId>
  <version>1.6.2</version>
</dependency>
```

- **Nota**: Debes incluir en el procesador de anotaciones del ```pom.xml```, el path hacia el procesador de la versión de mapstruct respectiva. En caso de no hacerlo, el procesador de anotaciones no podrá reconocer el bean de cada mapeador creado y por ende tendremos errores de ejecución.

```xml
<path>
  <groupId>org.mapstruct</groupId>
  <artifactId>mapstruct-processor</artifactId>
  <version>1.6.2</version>
</path>
```

8. **Dependencias para generación de getters, setters y constructores**

Lombok es una libreria escencial para la generación de getters, setters y constructores.

```xml
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>
```

**Nota:** Recuerda verificar en el procesador de anotaciones del ```pom.xml```, que el path hacia el procesador de anotaciones de Lombok se incluya (Aunque siempre se incluye por defecto si se ha seleccionado previamente la libreria desde Spring initializr)

9. **Dependencias para documentar la API**

Con Swagger UI y OpenAPI podremos crear una interfaz de usuartio para la documentación de una API Rest. A continuación, se detallan las dependencias necesarias para lograrlo.

```xml
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

10. **Dependencias para visualizar cambios en tiempo de ejecución (opcional)**

Spring Devtools es una dependencia importante que nos proporciona visualización de cambios, mientras nuestra app está en ejecución. Con lo anterior, y una configuración adecuada de nuestro IDE, nos evitaremos detener una app para incluir cambios que se hayan ello durante el tiempo de ejecución de la misma.

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-devtools</artifactId>
  <scope>runtime</scope>
  <optional>true</optional>
</dependency>
```
---

## Sección de Usuarios

### Crear Usuario

Para crear un usuario se debe enviar un json en el cuerpo de la solicitud con la siguiente estructura

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

La solcitud es procesada en el siguiente método, el cual llama al servicio para crear al usuario. Si el usuario se crea sin problema, se devuelve un json que informa al usuario sobre la creación exitosa del registro en la DB.

   ```Java
    @PostMapping("/registrar")
    public ResponseEntity<?> createRegister(@RequestBody @Valid UsuarioDTO dto) {
        usuarioService.crearUsuario(dto);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Usuario creado correctamente."
        ));
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


