package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Domain.Repositories.CursoRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarExistenciaCursoEnTopico implements Validador<TopicDTO>{

    private final CursoRepository cursoRepository;

    @Override
    public void validar(TopicDTO dto) throws EntityExistsException{
        cursoRepository.findByNombreIgnoreCase(dto.nombreCurso()).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado un curso con el nombre: " + dto)
        );
    }
}
