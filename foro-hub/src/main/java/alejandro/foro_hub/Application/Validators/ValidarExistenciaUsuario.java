package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Application.DTOs.UsuarioDTO;
import alejandro.foro_hub.Domain.Repositories.UsuarioRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidarExistenciaUsuario implements Validador<UsuarioDTO>{

    private final UsuarioRepository usuarioRepository;

    @Override
    public void validar(UsuarioDTO dto) throws EntityExistsException {
        if (usuarioRepository.existsByEmail(dto.email())){
            throw new EntityExistsException("Ya existe un usuario con el correo dado: " + dto.email());
        }
    }
}
