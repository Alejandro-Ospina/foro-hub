package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.RespuestaDto;
import alejandro.foro_hub.Application.DTOs.RespuestaUpdateDto;
import alejandro.foro_hub.Application.Mappers.RespuestaMapper;
import alejandro.foro_hub.Application.Services.RespuestaService;
import alejandro.foro_hub.Application.Validators.AuthenticationValidator;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.*;
import alejandro.foro_hub.Domain.Repositories.RespuestaRepository;
import alejandro.foro_hub.Domain.Repositories.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RespuestaServiceImplementations implements RespuestaService {

    private final RespuestaRepository respuestaRepository;
    private final RespuestaMapper mapper;
    private final List<Validador<RespuestaDto>> validador;
    private final TopicoRepository topicoRepository;
    private final AuthenticationValidator<Object> authenticationValidator;

    @Override
    public void crearRespuesta(RespuestaDto respuestaDto, Authentication authentication) {
        validador.forEach(validador -> validador.validar(respuestaDto));
        Respuesta respuesta = mapper.dtoToEntity(respuestaDto);

        Topico topico = topicoRepository.findByTituloIgnoreCase(respuestaDto.nombreTopico()).orElseThrow();
        respuesta.setTopico(topico);

        var userAuthenticated = authenticationValidator.getAuthenticationInstance(authentication);
        if (userAuthenticated instanceof Usuario) respuesta.setAutor((Usuario) userAuthenticated);
        if (userAuthenticated instanceof GoogleUser) respuesta.setAutor((GoogleUser) userAuthenticated);

        respuestaRepository.save(respuesta);
    }

    @Override
    @Transactional
    public void actualizarRespuesta(RespuestaUpdateDto respuestaDto,
                                    Long id,
                                    Authentication authentication) throws PermissionDeniedException {
        Respuesta respuesta = respuestaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado repsuesta con id: " + id)
        );

        validateUser(authentication, respuesta);
        mapper.updateEntityFromDto(respuestaDto, respuesta);
    }

    @Override
    public void eliminarRespuesta(Long id, Authentication authentication) throws PermissionDeniedException {
        Respuesta respuesta = respuestaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado repsuesta con id: " + id)
        );

        validateUser(authentication, respuesta);
        respuestaRepository.delete(respuesta);
    }

    private void validateUser(Authentication authentication, Respuesta respuesta) throws PermissionDeniedException {
        var userAuthenticated = authenticationValidator.getAuthenticationInstance(authentication);

        if (userAuthenticated instanceof Usuario usuarioLocal &&
                !Objects.equals(usuarioLocal.getId(), respuesta.getAutor().getId()))
            throw new PermissionDeniedException("No tiene permisos para editar la respuesta");

        if (userAuthenticated instanceof GoogleUser googleUser &&
                !Objects.equals(googleUser.getId(), respuesta.getAutor().getId()))
            throw new PermissionDeniedException("No tiene permisos para editar la respuesta");
    }
}
