package alejandro.foro_hub.Application.DTOs;

import jakarta.validation.constraints.NotEmpty;

public record GoogleUserDto(
        @NotEmpty
        String sub,

        @NotEmpty
        String nombre,

        @NotEmpty
        String email,

        @NotEmpty
        String foto
) {
}
