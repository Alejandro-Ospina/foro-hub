package alejandro.foro_hub.Infrastructure.Adapters.Inputs;

import alejandro.foro_hub.Application.Controllers.BaseController;
import alejandro.foro_hub.Application.DTOs.CursoDTO;
import alejandro.foro_hub.Application.DTOs.ResponseEntityDto;
import alejandro.foro_hub.Application.Mappers.CursoMapper;
import alejandro.foro_hub.Application.Services.CursoService;
import alejandro.foro_hub.Domain.Repositories.CursoRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping ("/cursos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class CursoController implements BaseController<CursoDTO, Long> {

    private final CursoService cursoService;
    private final CursoRepository cursoRepository;
    private final CursoMapper mapper;

    @Override
    @PostMapping
    public ResponseEntity<?> createRegister(@RequestBody @Valid CursoDTO dto) {
        cursoService.crearCurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.CREATED.value(),
                "Curso creado correctamente."
        ));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRegister(@PathVariable @Positive Long id) {
        cursoService.eliminarCurso(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.OK.value(),
                "Curso eliminado correctamente."
        ));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRegister(@RequestBody @Valid CursoDTO dto, @PathVariable @Positive Long id) {
        cursoService.actualizarCurso(dto, id);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.ACCEPTED.value(),
                "Curso actualizado correctamente."
        ));
    }

    @GetMapping
    public ResponseEntity<?> getCursosList(){
        return ResponseEntity.ok(
                cursoRepository.findAll().stream().map(mapper::cursoToDto).toList()
        );
    }
}
