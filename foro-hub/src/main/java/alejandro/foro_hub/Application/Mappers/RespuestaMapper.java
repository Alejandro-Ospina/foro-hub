package alejandro.foro_hub.Application.Mappers;

import alejandro.foro_hub.Application.DTOs.RespuestaDto;
import alejandro.foro_hub.Application.DTOs.RespuestaUpdateDto;
import alejandro.foro_hub.Domain.Models.Respuesta;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface RespuestaMapper {

    Respuesta dtoToEntity (RespuestaDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(RespuestaUpdateDto dto, @MappingTarget Respuesta respuesta);
}
