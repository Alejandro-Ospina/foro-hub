package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Domain.Repositories.TopicoRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarExistenciaTopico implements Validador<TopicDTO> {

    private final TopicoRepository topicoRepository;

    @Override
    public void validar(TopicDTO dto) throws EntityExistsException {
        topicoRepository.findByTituloAndMensajeIgnoreCase(dto.titulo(), dto.mensaje())
                .ifPresent(
                        topico -> {
                            throw new EntityExistsException("Ya existe un tópico con un título y mensaje dados.");
                        }
                );
    }
}
