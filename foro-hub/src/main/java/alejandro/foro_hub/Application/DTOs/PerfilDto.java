package alejandro.foro_hub.Application.DTOs;

import jakarta.validation.constraints.NotEmpty;

public record PerfilDto(@NotEmpty String nombre) {
}
