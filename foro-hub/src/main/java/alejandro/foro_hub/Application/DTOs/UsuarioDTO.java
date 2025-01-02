package alejandro.foro_hub.Application.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UsuarioDTO(

        @NotEmpty
        @Size (max = 100)
        String nombre,

        @NotEmpty
        @Email
        @Size(max = 100)
        String email,

        @NotEmpty
        String pass,

        @NotNull
        @NotEmpty
        @Valid
        Set<PerfilDto> perfiles
){
}