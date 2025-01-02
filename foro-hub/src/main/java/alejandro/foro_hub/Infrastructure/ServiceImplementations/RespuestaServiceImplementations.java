package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.RespuestaDto;
import alejandro.foro_hub.Application.DTOs.RespuestaUpdateDto;
import alejandro.foro_hub.Application.Mappers.RespuestaMapper;
import alejandro.foro_hub.Application.Services.RespuestaService;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.Respuesta;
import alejandro.foro_hub.Domain.Models.Topico;
import alejandro.foro_hub.Domain.Models.Usuario;
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

    @Override
    public void crearRespuesta(RespuestaDto respuestaDto, Authentication authentication) {
        validador.forEach(validador -> validador.validar(respuestaDto));
        Respuesta respuesta = mapper.dtoToEntity(respuestaDto);

        Topico topico = topicoRepository.findByTituloIgnoreCase(respuestaDto.nombreTopico()).orElseThrow();
        Usuario usuario = (Usuario) authentication.getPrincipal();

        respuesta.setTopico(topico);
        respuesta.setAutor(usuario);

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

        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (!Objects.equals(respuesta.getAutor().getEmail(), usuario.getEmail())){
            throw new PermissionDeniedException("Usted no tiene permisos para editar la respuesta");
        }

        mapper.updateEntityFromDto(respuestaDto, respuesta);
    }

    @Override
    public void eliminarRespuesta(Long id, Authentication authentication) throws PermissionDeniedException {
        Respuesta respuesta = respuestaRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado repsuesta con id: " + id)
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        if (!Objects.equals(respuesta.getAutor().getEmail(), usuario.getEmail())){
            throw new PermissionDeniedException("Usted no tiene permisos para eliminar la respuesta");
        }

        respuestaRepository.delete(respuesta);
    }
}
