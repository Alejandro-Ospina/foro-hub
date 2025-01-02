package alejandro.foro_hub.Application.DTOs;

import alejandro.foro_hub.Domain.Models.Categoria;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CursoDTO(

        @NotEmpty
        @Size(max = 255)
        String nombre,

        @NotNull
        Categoria categoria
){
}
