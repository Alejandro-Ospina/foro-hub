package alejandro.foro_hub.Application.Services;

import alejandro.foro_hub.Application.DTOs.ActualizarUsuarioDto;
import alejandro.foro_hub.Application.DTOs.UsuarioDTO;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import org.springframework.security.core.Authentication;

public interface UsuarioService {

    void crearUsuario(UsuarioDTO usuarioDTO);
    void actualizarUsuario(ActualizarUsuarioDto dto, Long id, Authentication authentication) throws PermissionDeniedException;
    void eliminarUsuario(Authentication authentication);
}
