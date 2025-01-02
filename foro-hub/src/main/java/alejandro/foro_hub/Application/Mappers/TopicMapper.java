package alejandro.foro_hub.Application.Mappers;

import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Domain.Models.Topico;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface TopicMapper {

    Topico dtoToEntity(TopicDTO topicDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(TopicDTO dto, @MappingTarget Topico entity);
}
