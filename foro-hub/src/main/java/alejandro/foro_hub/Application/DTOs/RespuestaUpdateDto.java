package alejandro.foro_hub.Application.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record RespuestaUpdateDto(
        @NotEmpty
        String mensaje,

        @NotNull
        LocalDateTime fechaCreacion,

        @NotEmpty
        String solucion
) {
}
