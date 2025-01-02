package alejandro.foro_hub.Application.Services;

import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import org.springframework.security.core.Authentication;

public interface TopicService {

    void saveTopic(TopicDTO topicDTO, Authentication authentication);
    void updateTopic(TopicDTO topicDTO, Long id, Authentication authentication) throws PermissionDeniedException;
    void deleteTopic(Long id);
}
