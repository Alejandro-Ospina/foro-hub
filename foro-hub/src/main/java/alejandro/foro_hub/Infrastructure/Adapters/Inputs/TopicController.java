package alejandro.foro_hub.Infrastructure.Adapters.Inputs;

import alejandro.foro_hub.Application.DTOs.ResponseEntityDto;
import alejandro.foro_hub.Application.DTOs.TopicDTO;
import alejandro.foro_hub.Application.DTOs.TopicShowDto;
import alejandro.foro_hub.Application.Services.TopicService;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.Topico;
import alejandro.foro_hub.Domain.Repositories.TopicoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping ("/topicos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class TopicController {

    private final TopicService topicService;
    private final TopicoRepository topicoRepository;

    @PostMapping
    public ResponseEntity<?> createRegister(@RequestBody @Valid TopicDTO dto, Authentication authentication) {
        topicService.saveTopic(dto, authentication);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Tópico creado correctamente."
        ));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegister(@PathVariable @Positive Long id) {
        topicService.deleteTopic(id);

        return ResponseEntity.accepted().body(
                new ResponseEntityDto(
                        LocalDateTime.now(),
                        HttpStatus.ACCEPTED.value(),
                        "Tópico eliminado correctamente."
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegister(@RequestBody @Valid TopicDTO dto,
                                            @PathVariable @Positive Long id,
                                            Authentication authentication) throws PermissionDeniedException {
        topicService.updateTopic(dto, id, authentication);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Tópico actualizado correctamente."
        ));
    }

    @GetMapping
    public ResponseEntity<?> showRegisters(){
        return ResponseEntity.ok(topicoRepository.findAllTopicsWithDetails().stream().map(TopicShowDto::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showTopic(@PathVariable @Positive Long id){
        Topico topico = topicoRepository.findTopicByIdWithDetails(id).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado un tópico con el id: " + id)
        );

        return ResponseEntity.ok(new TopicShowDto(topico));
    }
}
