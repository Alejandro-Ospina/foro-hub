package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Application.Mappers.TopicMapper;
import alejandro.foro_hub.Application.Services.TopicService;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.Curso;
import alejandro.foro_hub.Domain.Models.Topico;
import alejandro.foro_hub.Domain.Models.Usuario;
import alejandro.foro_hub.Domain.Repositories.CursoRepository;
import alejandro.foro_hub.Domain.Repositories.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImplementations implements TopicService {

    private final List<Validador<TopicDTO>> validador;
    private final TopicoRepository topicoRepository;
    private final TopicMapper mapper;
    private final CursoRepository cursoRepository;

    @Override
    public void saveTopic(TopicDTO topicDTO, Authentication authentication){
        validador.forEach(
                validar -> validar.validar(topicDTO)
        );

        Usuario usuario = (Usuario) authentication.getPrincipal();
        Curso curso = cursoRepository.findByNombreIgnoreCase(topicDTO.nombreCurso()).orElseThrow();

        Topico topico = mapper.dtoToEntity(topicDTO);
        topico.setAutor(usuario);
        topico.setCurso(curso);
        topicoRepository.save(topico);
    }

    @Override
    @Transactional
    public void updateTopic(TopicDTO topicDTO, Long id, Authentication authentication) throws PermissionDeniedException {
        Topico topico = topicoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado tópico con id: " + id)
        );

        if (!topico.getAutor().getUsername().equals(authentication.getName())){
            throw new PermissionDeniedException("No tiene permisos para editar el topico");
        }

        mapper.updateEntityFromDto(topicDTO, topico);
    }

    @Override
    public void deleteTopic(Long id, Authentication authentication) throws PermissionDeniedException {
        Topico topico = topicoRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado tópico con id: " + id)
        );

        if (!topico.getAutor().getUsername().equals(authentication.getName())){
            throw new PermissionDeniedException("No tiene permisos para eliminar el topico");
        }

        topicoRepository.delete(topico);
    }
}
