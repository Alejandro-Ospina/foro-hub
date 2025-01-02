package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Application.DTOs.CursoDTO;
import alejandro.foro_hub.Domain.Repositories.CursoRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarExistenciaCurso implements Validador<CursoDTO> {

    private final CursoRepository cursoRepository;

    @Override
    public void validar(CursoDTO dto) throws EntityExistsException {
        cursoRepository.findByNombreIgnoreCase(dto.nombre()).ifPresent(
                error -> {
                    throw new EntityExistsException("Ya existe un curso con el nombre: " + dto.nombre());
                }
        );
    }
}
