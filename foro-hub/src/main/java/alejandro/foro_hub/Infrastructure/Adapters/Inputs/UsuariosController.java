package alejandro.foro_hub.Infrastructure.Adapters.Inputs;

import alejandro.foro_hub.Application.DTOs.*;
import alejandro.foro_hub.Application.Services.JwtService;
import alejandro.foro_hub.Application.Services.UsuarioService;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.Usuario;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class UsuariosController{

    private final JwtService jwtService;
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/registrar")
    public ResponseEntity<?> createRegister(@RequestBody @Valid UsuarioDTO dto) {
        usuarioService.crearUsuario(dto);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Usuario creado correctamente."
        ));
    }

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

    @DeleteMapping("/eliminar")
    public ResponseEntity<?> deleteRegister(Authentication authentication) {
        usuarioService.eliminarUsuario(authentication);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.ACCEPTED.value(),
                "Usuario dado de baja correctamente."
        ));
    }

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
}
