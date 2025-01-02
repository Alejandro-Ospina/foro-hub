package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Application.DTOs.RespuestaDto;
import alejandro.foro_hub.Domain.Repositories.TopicoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarExistenciaTopicoEnRespuesta implements Validador<RespuestaDto> {

    private final TopicoRepository topicoRepository;

    @Override
    public void validar(RespuestaDto dto) throws EntityExistsException {
        topicoRepository.findByTituloIgnoreCase(dto.nombreTopico()).orElseThrow(
                () -> new EntityNotFoundException("No hay un tópico con título: " + dto.nombreTopico())
        );
    }
}
