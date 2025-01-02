package alejandro.foro_hub.Application.Mappers;

import alejandro.foro_hub.Application.DTOs.CursoDTO;
import alejandro.foro_hub.Domain.Models.Curso;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CursoMapper {

    Curso dtoToCurso(CursoDTO cursoDTO);
    CursoDTO cursoToDto(Curso curso);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(CursoDTO dto, @MappingTarget Curso curso);
}
