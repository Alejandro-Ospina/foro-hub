package alejandro.foro_hub.Application.Services;

import alejandro.foro_hub.Application.DTOs.CursoDTO;

public interface CursoService {

    void crearCurso(CursoDTO cursoDTO);
    void actualizarCurso(CursoDTO cursoDTO, Long id);
    void eliminarCurso(Long id);
}
