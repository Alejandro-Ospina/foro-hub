package alejandro.foro_hub.Infrastructure.Adapters.Inputs;

import alejandro.foro_hub.Application.DTOs.ResponseEntityDto;
import alejandro.foro_hub.Application.DTOs.RespuestaDto;
import alejandro.foro_hub.Application.DTOs.RespuestaShowDto;
import alejandro.foro_hub.Application.DTOs.RespuestaUpdateDto;
import alejandro.foro_hub.Application.Services.RespuestaService;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.Usuario;
import alejandro.foro_hub.Domain.Repositories.RespuestaRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    private final RespuestaService respuestaService;
    private final RespuestaRepository respuestaRepository;

    @PostMapping
    public ResponseEntity<?> createAnswer(@RequestBody @Valid RespuestaDto respuestaDto, Authentication authentication){
        respuestaService.crearRespuesta(respuestaDto, authentication);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Respuesta creada correctamente."
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAnswer(@RequestBody @Valid RespuestaUpdateDto dto,
                                          @PathVariable @Positive Long id,
                                          Authentication authentication) throws PermissionDeniedException {
        respuestaService.actualizarRespuesta(dto, id, authentication);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Respuesta editada correctamente."
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnswer(@PathVariable @Positive Long id,
                                          Authentication authentication) throws PermissionDeniedException {
        respuestaService.eliminarRespuesta(id, authentication);

        return ResponseEntity.ok(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.ACCEPTED.value(),
                "Respuesta eliminada correctamente."
        ));
    }

    @GetMapping
    public ResponseEntity<?> getUserAnswersList(@PageableDefault(size = 10, sort = "id") Pageable pageable,
                                                Authentication authentication){
        Usuario usuario = (Usuario) authentication.getPrincipal();
        return ResponseEntity.ok(
                respuestaRepository.userAnswersList(usuario.getEmail(), pageable)
                        .map(RespuestaShowDto::new)
        );
    }
}
