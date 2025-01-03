# Foro Hub Alura

Foro Hub Alura es una API desarrollada como parte de un reto formativo proporcionado por Alura Latam y Oracle. Este proyecto implementa un sistema de foros que permite a los usuarios interactuar en temas, responder publicaciones y gestionar recursos de manera segura mediante autenticación JWT.

## Índice de Contenidos

1. [Tecnologías Usadas](#tecnologias-usadas)
2. [Configuración del Proyecto](#configuracion-del-proyecto)
3. [Configuración de Variables de Entorno](#configuracion-de-variables-de-entorno)
4. [Configuración de la Base de Datos PostgreSQL](#configuracion-de-la-base-de-datos-postgresql)
5. [Dependencias Necesarias](#dependencias-necesarias)
6. [Usuarios](#usuarios)
7. [Tópicos](#topicos)
8. [Respuestas y Cursos](#respuestas-y-cursos)
9. [Ejecución del JAR en Local](#ejecucion-del-jar-en-local)
10. [Agradecimientos](#agradecimientos)

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

## Usuarios

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

La solicitud es procesada en el siguiente método, el cual valida los campos de entrada, y llama al servicio para crear al usuario. Si el usuario se crea sin problema, se devuelve un json que informa al usuario sobre la creación exitosa del registro en la DB.

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

Cuando el controlador invoca el servicio, este se encarga de validar si el usuario existe. En caso contrario, se procede con el mapeo entidad-dto, se encripta la contraseña con el algortimo de hash Bcrypt, y se guarda el registro en la base de datos.

   ```Java
    @Override
    public void crearUsuario(UsuarioDTO usuarioDTO) {
        validar.forEach(validar -> validar.validar(usuarioDTO));

        Usuario usuario = mapper.dtoToEntity(usuarioDTO);
        usuario.setPass(encoder.encode(usuario.getPass()));
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }
   ```

### Actualizar usuario

Para actualizar un usuario se debe enviar un json el endpoint mostrado, y con la estructura:

```http
POST /usuarios/{id}
{
    "nombre": "Pedro aurelio ospina benitez",
    "email": "pedro8765@gmail.com",
    "perfiles": [
        {
            "nombre": "dev80006"
        },
        {
            "nombre": "alejo1000"
        },
        {
            "nombre": "orco67999123"
        }
    ]
}
```

La información se procesa en el siguiente método, el cual recibe de manera adicional la información del usuario auitenticado.

```Java
@PutMapping("/{id}")
public ResponseEntity<?> updateRegister(@RequestBody @Valid ActualizarUsuarioDto dto,
                                        @PathVariable @Positive Long id,
                                        Authentication authentication) throws PermissionDeniedException {
    usuarioService.actualizarUsuario(dto, id, authentication);

    return ResponseEntity.ok(new ResponseEntityDto(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Usuario actualizado correctamente."
    ));
}
```

El método, invoca el servicio, y este se encarga de validar; además de la existencia del usuario, si los datos que se van a actualizar son del usuario autenticado, es decir, propietario. En caso contrario, se le denegará el permiso de edición, ya que a nivel de   lógica un usuario mo puede editar datos de otros dominio.

```Java
@Override
@Transactional
public void actualizarUsuario(ActualizarUsuarioDto dto,
                              Long id,
                              Authentication authentication) throws PermissionDeniedException {
    Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

    Usuario usuario = usuarioRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("No se encontró un usuario con id: " + id)
    );

    if (!Objects.equals(usuario.getId(), usuarioAutenticado.getId())){
        throw new PermissionDeniedException("No tiene permiso para editar el usuario");
    }

    mapper.updateEntityFromDto(dto, usuario);
}
```

### Eliminar usuario

Para eliminar un usuario, simplemente se envía una solicitud de tipo `delete` al endpoint `/usuarios`, y el siguiente método la procesará:

```Java
@Override
@DeleteMapping("/eliminar")
public ResponseEntity<?> deleteRegister(Authentication authentication) {
    usuarioService.eliminarUsuario(authentication);

    return ResponseEntity.ok(new ResponseEntityDto(
            LocalDateTime.now(),
            HttpStatus.ACCEPTED.value(),
            "Usuario dado de baja correctamente."
    ));
}
```

El controlador invoca el servicio de usuarios, y este eliminará lógicamente el registro en la base de datos. Nótese que no es necesario enviar un id específico, ya que el único parámetro es el usuario autenticado, es decir, un usuario no puede eliminar registro de otros usuarios, solo es posible los de su autoría. 

```Java
@Override
@Transactional
public void eliminarUsuario(Authentication authentication) {
    Usuario usuario = usuarioRepository.findByNombre(authentication.getName()).orElseThrow(
            () -> new EntityNotFoundException("No se ha encontrado el usuario: " + authentication.getName())
    );

    usuario.setActivo(false);
}
```

### Ingreso de usuarios

Para el ingreso de usuarios a recursos protegidos, se debem enviar las siguientes credenciales al siguiente endpoint con la siguiente estructura json:

```http
POST /usuarios/login
{
    "email": "ejemplo@gmail.com",
    "password": "pass"
}
```

La solicitud es recibida en el siguiente método, el cual se encarga de crear una instancia de autenticación con el correo y contraseña pasados en el cuerpo de la solicitud.

```Java
@PostMapping("/login")
public ResponseEntity<?> inicioSesion(@RequestBody @Valid LoginDto loginDto){
    Authentication authentication = new UsernamePasswordAuthenticationToken(
            loginDto.email(),
            loginDto.password()
    );

    var usuario = authenticationManager.authenticate(authentication);
    var token = jwtService.obtenerToken((Usuario) usuario.getPrincipal());

    return ResponseEntity.ok(new TokenDTO(usuario.getName(), token));
}
```

Luego, el administrador de autenticación busca si coincide el correo y la contraseña con el registro de la DB, y crea una instancia de usuario autenticado. Posteriormente, se obtiene un token del usuario autenticado por medio del servicio JWT, y se devuelve en la respuesta del método un json con la información del token generado.

### Generación del token de autenticación con JWT

Cuando el controlador de inicio de sesión hace llamado al servicio JWT, este se encarga de procesar la información del usuario (id, nombre y correo electrónico) y la compacta en una cadena de caracteres de 512 bits. Cabe aclarar que pueden anexarse mas claims en el payload del jwt dependiendo del caso.

```Java
@Override
public String obtenerToken(Usuario usuario) {
    return Jwts.builder()
            .issuer("Alejandro")
            .subject(usuario.getEmail())
            .claim("id", usuario.getId())
            .claim("nombre", usuario.getNombre())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusSeconds(3600)))
            .signWith(getKeyEncoded(), Jwts.SIG.HS512)
            .compact();
}
```

Además, se define un tiempo de expiración de una hora, y se debe definir una clave de mínimo 64 caracteres `UTF_8` la cual se firma con un algoritmo hashing de 512 bits. Finalmente, se compacta el token generado y se devuelve en la llamada del método.

**Nota:** Para este proyecto se debe definir una clave mayor o igual a 64 caracteres debido a que la firma se hace con un algoritmo de firma `HS512`.

### Validación del token

En el contexto de autenticación por medio de tokens, es escencial verificar si un token es válido o no. `jjwt` Nos proporciona una serie de herramientas que nos permite validar el origen de un token tal y como se muestra en el siguiente método:

```Java
@Override
public Boolean tokenValido(String token) {
    Claims claims;
    try{
        claims = Jwts.parser()
                .verifyWith(getKeyEncoded())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration().after(new Date());
    } catch (JwtException | IllegalArgumentException e) {
        return false;
    }
}
```

Lo primero que se debe hacer es verificar la firma del token, y para ello, pasamos en el argumento del método `verifyWith()` la clave con la cual hemos firmado nuestros tokens. Si no lanza una excepción, se construye un objeto vacío con los claims declarados en la construcción del token. Posteriormente se llena la información de los claims con el token recibido, y se obtiene el payload completo del token. Si hasta este punto no se ha lanzado una excepción, entonces se puede afirmar parcialmente que el token es confiable. 

Para que nuestro token no sea parcialmente válido, debemos corroborar que no ha expirado, y solo es allí cuando podemos confiar en el origen del token. Es por ello que se necesita obtener los claims del token, ya que así podemos extraer la fecha de expiración y compararla con el instante en que se consulta. De esa forma, retornamos un valor verdaero; y, si en algún momento atrapa una excepción, entonces no podemos confiar en el origen del token.

* **Nota:** Es importante aclarar que si bien, este proceso es básico, refleja una de muchas formas de trabajar con Jwt. Es importante que usted como lector verifique la documentación oficial de los proveedores, y haga su propia implementación de acuerdo a sus necesidades.

### Obtención del subject, o sujeto

Cuando se estructura un jwt, es importante definir dinámicamenre aquello que se va a declarar como subject, o sujeto del token. En este caso, el subject definido es el correo electrónico de usuario, pues, además de ser único por usuario, es un estándar en el inicio de sesión en la gran mayoria de aplicaciones existentes.

```Java
@Override
public String obtenerSujeto(String token) {
    if(token == null || token.isEmpty()) throw new TokenNullException("El token es vacío o nulo.");

    String sujeto;
    try{
        return Jwts.parser()
                .verifyWith(getKeyEncoded())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    } catch (JwtException e) {
        throw new TokenInvalidException("No se ha obtenido el sujeto debido a token no válido.");
    }
}
```

El primer paso es verificar si el token que ingresa es nulo o vacío. Comprobado lo anterior, se sigue un proceso bastante parecido a la [validación del token](#validacion-del-token), solo que en este caso cuando obtenemos el payload, devolvemos el subject.

### Configuración de la clave secreta

La clave secreta, definida previamente como variable de entorno `SECRET`, debe ser una cadena de caracteres con longitud mínima de 64. Esta clave no debe ingresar de forma directa en la firma del algoritmo definido en la firma, sino que debe ser convertida a un arreglo de bytes en su representación `UTF_8`, y encriptada por un algoritmo `HMAC`.

```Java
private SecretKey getKeyEncoded(){
    return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
}
```

3. **Autorización**: Todas las solicitudes protegidas requieren el token en el encabezado `Authorization`:
   ```http
   Authorization: Bearer <token>
   ```

### Filtro de Autenticación e Interceptores

Antes de realizar cualquier petición a la API, se debe configurar un filtro de autenticación donde se valida el JWT, y se determina si el usuario está activo. 

```Java
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

## Tópicos

### Crear tópico

Para crear un tópico, se debe hacer debe enviar un json con el siguiente formato al siguiente endpoint:

```http
POST /topicos
{
    "titulo": "Eventos",
    "mensaje": "Este tópico contiene contenido relacionado a eventos javascritp",
    "fechaCreacion": "2024-12-26T16:20:00",
    "status": "true",
    "nombreCurso": "desarrollo backend con javascript"
}
```

El siguiente método recibe la información necesaria para poder crear el tópico, en este caso: el json anterior, y el usuario autenticado.

```Java
@PostMapping
public ResponseEntity<?> createRegister(@RequestBody @Valid TopicDTO dto, Authentication authentication) {
    topicService.saveTopic(dto, authentication);

    return ResponseEntity.ok(new ResponseEntityDto(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Tópico creado correctamente."
    ));
}
```

El controlador invoca el servicio de tópicos, y guarda un tópico en la base de datos. El servicio encargado se verificar la existencia del curso con el nombre dado en el json, y luego hace un mapeo de los campos necesarios a un registro de la entidad tópico. Finalmente, se terminan de settear los parámetros de Autor y Curso con la instancia de autenticación (usuario de la solicitud), y el registro encontrado en la tabla de cursos respectivamente:

```Java
@Override
public void saveTopic(TopicDTO topicDTO, Authentication authentication){
    validador.forEach(
            validar -> validar.validar(topicDTO)
    );

    Usuario usuario = (Usuario) authentication.getPrincipal();
    Curso curso = cursoRepository.findByNombreIgnoreCase(topicDTO.nombreCurso()).orElseThrow();

    Topico topico = mapper.dtoToEntity(topicDTO);
    topico.setAutor(usuario);
    topico.setCurso(curso);
    topicoRepository.save(topico);
}
```

### Actualizar tópico

La actualización debe hacerse en el siguiente endpoint con el siguiente formato json:

```http
PUT /topicos/{id}
{
    "titulo": "Event listeners 2",
    "mensaje": "Este tópico contiene contenido relacionado a event listeners 2 en javascritp",
    "fechaCreacion": "2024-12-27T17:20:00",
    "status": "true",
    "nombreCurso": "desarrollo backend con javascript"
}
```

Luego, el siguiente método recibe los parámetros para actualizar el curso, además del id del tópico y el usuario autenticado:

```Java
@PutMapping("/{id}")
public ResponseEntity<?> updateRegister(@RequestBody @Valid TopicDTO dto,
                                        @PathVariable @Positive Long id,
                                        Authentication authentication) throws PermissionDeniedException {
    topicService.updateTopic(dto, id, authentication);

    return ResponseEntity.ok(new ResponseEntityDto(
            LocalDateTime.now(),
            HttpStatus.OK.value(),
            "Tópico actualizado correctamente."
    ));
}
```

Posteriormente, se invoca el servicio, donde se evalua en primer lugar si existe el tópico con el id dado. Después, se verifica si el autor del tópico corresponde con el usuario que está autenticado. En caso de no coincidir el usuario, se le denegarán los permisos de edición, pues no se puede editar un recurso que no es de autoría propia. Finalmente, se mappean los datos necesarios, y se settean los valores del registro a actualizar.

```Java
@Override
@Transactional
public void updateTopic(TopicDTO topicDTO, Long id, Authentication authentication) throws PermissionDeniedException {
    Topico topico = topicoRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("No se ha encontrado tópico con id: " + id)
    );

    if (!topico.getAutor().getUsername().equals(authentication.getName())){
        throw new PermissionDeniedException("No tiene permisos para editar el topico");
    }

    mapper.updateEntityFromDto(topicDTO, topico);
}
```

### Eliminar tópico

Para eliminar un tópico, se debe hacer una solicitud de tipo `DELETE` al endpoint `/topicos/{id}`.

```Java
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteRegister(@PathVariable @Positive Long id) {
    topicService.deleteTopic(id);

    return ResponseEntity.accepted().body(
            new ResponseEntityDto(
                    LocalDateTime.now(),
                    HttpStatus.ACCEPTED.value(),
                    "Tópico eliminado correctamente."
            )
    );
}
```

El controlador invoca el servicio para eliminar un registro con id específico de la base de datos; sin embargo, antes de eliminar, el servicio corrobora si existe el tópico, y si el usuario que está autenticado es el autor del tópico. Al igual que el caso anterior, no se puede eliminar o editar un elemento que no es de autoría propia.

```Java
@Override
public void deleteTopic(Long id) {
    Topico topico = topicoRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("No se ha encontrado tópico con id: " + id)
    );

    topicoRepository.delete(topico);
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


