package alejandro.foro_hub.Application.Mappers;

import alejandro.foro_hub.Application.DTOs.ActualizarUsuarioDto;
import alejandro.foro_hub.Application.DTOs.UsuarioDTO;
import alejandro.foro_hub.Domain.Models.Usuario;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface UsuarioMapper{

    Usuario dtoToEntity(UsuarioDTO usuarioDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(ActualizarUsuarioDto dto, @MappingTarget Usuario usuario);
}
