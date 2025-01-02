package alejandro.foro_hub.Application.Services;

import alejandro.foro_hub.Application.DTOs.RespuestaDto;
import alejandro.foro_hub.Application.DTOs.RespuestaUpdateDto;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import org.springframework.security.core.Authentication;

public interface RespuestaService {

    void crearRespuesta(RespuestaDto respuestaDto, Authentication authentication);
    void actualizarRespuesta(RespuestaUpdateDto dto, Long id, Authentication authentication) throws PermissionDeniedException;
    void eliminarRespuesta(Long id, Authentication authentication) throws PermissionDeniedException;
}
