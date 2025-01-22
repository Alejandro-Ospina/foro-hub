package alejandro.foro_hub.Application.Mappers;

import alejandro.foro_hub.Application.DTOs.GoogleUserDto;
import alejandro.foro_hub.Domain.Models.GoogleUser;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        componentModel = "spring"
)
public interface GoogleUserMapper {

    GoogleUser dtoToEntity(GoogleUserDto dto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(GoogleUserDto dto, @MappingTarget GoogleUser entity);
}
