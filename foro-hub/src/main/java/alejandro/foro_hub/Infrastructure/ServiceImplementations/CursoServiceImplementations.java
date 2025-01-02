package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.CursoDTO;
import alejandro.foro_hub.Application.Mappers.CursoMapper;
import alejandro.foro_hub.Application.Services.CursoService;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Models.Curso;
import alejandro.foro_hub.Domain.Repositories.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoServiceImplementations implements CursoService {

    private final CursoRepository cursoRepository;
    private final List<Validador<CursoDTO>> validador;
    private final CursoMapper mapper;

    @Override
    public void crearCurso(CursoDTO cursoDTO) {
        validador.forEach(
                validar -> validar.validar(cursoDTO)
        );

        Curso curso = mapper.dtoToCurso(cursoDTO);
        cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void actualizarCurso(CursoDTO cursoDTO, Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No existe curso con id: " + id)
        );

        mapper.updateEntityFromDto(cursoDTO, curso);
    }

    @Override
    public void eliminarCurso(Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado curso con id: " + id)
        );

        cursoRepository.delete(curso);
    }
}
