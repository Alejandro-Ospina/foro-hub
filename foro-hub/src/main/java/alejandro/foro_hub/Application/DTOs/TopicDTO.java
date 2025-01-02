package alejandro.foro_hub.Application.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TopicDTO(

        @NotEmpty
        @Size (max = 255)
        String titulo,

        @NotEmpty
        @Size (max = 1000)
        String mensaje,

        @NotNull
        LocalDateTime fechaCreacion,

        @NotNull
        Boolean status,

        @NotEmpty
        String nombreCurso
) {
}
