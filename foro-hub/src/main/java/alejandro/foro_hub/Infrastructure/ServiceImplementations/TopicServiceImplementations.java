package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Application.Mappers.TopicMapper;
import alejandro.foro_hub.Application.Services.TopicService;
import alejandro.foro_hub.Application.Validators.AuthenticationValidator;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.*;
import alejandro.foro_hub.Domain.Repositories.CursoRepository;
import alejandro.foro_hub.Domain.Repositories.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TopicServiceImplementations implements TopicService {

    private final List<Validador<TopicDTO>> validador;
    private final TopicoRepository topicoRepository;
    private final TopicMapper mapper;
    private final CursoRepository cursoRepository;
    private final AuthenticationValidator<Object> authenticationValidator;

    @Override
    public void saveTopic(TopicDTO topicDTO, Authentication authentication){
        validador.forEach(
                validar -> validar.validar(topicDTO)
        );

        var userAuthenticated = authenticationValidator.getAuthenticationInstance(authentication);
        Curso curso = cursoRepository.findByNombreIgnoreCase(topicDTO.nombreCurso()).orElseThrow();
        Topico topico = mapper.dtoToEntity(topicDTO);
        topico.setCurso(curso);

        if (userAuthenticated instanceof Usuario localUser) topico.setAutor(localUser);
        if (userAuthenticated instanceof GoogleUser googleUser) topico.setAutor(googleUser);

        topicoRepository.save(topico);
    }

    @Override
    @Transactional
    public void updateTopic(TopicDTO topicDTO, Long id, Authentication authentication) throws PermissionDeniedException {
        Topico topico = topicoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado tópico con id: " + id)
        );

        var usuario = topico.getAutor();
        validateUser(usuario, authentication);

        mapper.updateEntityFromDto(topicDTO, topico);
    }

    @Override
    public void deleteTopic(Long id, Authentication authentication) throws PermissionDeniedException {
        Topico topico = topicoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado tópico con id: " + id)
        );

        var usuario = topico.getAutor();
        validateUser(usuario, authentication);

        topicoRepository.delete(topico);
    }

    private void validateUser(UsuarioBase usuario, Authentication authentication) throws PermissionDeniedException {
        var userAuthenticated = authenticationValidator.getAuthenticationInstance(authentication);
        if (userAuthenticated instanceof Usuario localUser &&
                !Objects.equals(localUser.getId(), usuario.getId()))
            throw new PermissionDeniedException("No tiene permisos para editar el topico");

        if (userAuthenticated instanceof GoogleUser googleUser &&
                !Objects.equals(googleUser.getId(), usuario.getId())){
            throw new PermissionDeniedException("No tiene permisos para editar el tópico.");
        }
    }
}
