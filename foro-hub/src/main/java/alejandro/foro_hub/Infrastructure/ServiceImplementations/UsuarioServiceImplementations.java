package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.ActualizarUsuarioDto;
import alejandro.foro_hub.Application.DTOs.UsuarioDTO;
import alejandro.foro_hub.Application.Mappers.UsuarioMapper;
import alejandro.foro_hub.Application.Services.UsuarioService;
import alejandro.foro_hub.Application.Validators.AuthenticationValidator;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.GoogleUser;
import alejandro.foro_hub.Domain.Models.Usuario;
import alejandro.foro_hub.Domain.Repositories.GoogleUserRepository;
import alejandro.foro_hub.Domain.Repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImplementations implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper mapper;
    private final List<Validador<UsuarioDTO>> validar;
    private final PasswordEncoder encoder;
    private final AuthenticationValidator<Object> authenticationValidator;
    private final GoogleUserRepository googleUserRepository;

    @Override
    public void crearUsuario(UsuarioDTO usuarioDTO) {
        validar.forEach(validar -> validar.validar(usuarioDTO));

        Usuario usuario = mapper.dtoToEntity(usuarioDTO);
        usuario.setPass(encoder.encode(usuario.getPass()));
        usuario.setActivo(true);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void actualizarUsuario(ActualizarUsuarioDto dto,
                                  Long id,
                                  Authentication authentication) throws PermissionDeniedException {

        var userAuthenticated = authenticationValidator.getAuthenticationInstance(authentication);
        if (userAuthenticated instanceof GoogleUser)
            throw new PermissionDeniedException("No tiene permisos para editar el usuario.");

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se encontró un usuario con id: " + id)
        );

        if (!Objects.equals(usuario.getId(), ((Usuario) userAuthenticated).getId())){
            throw new PermissionDeniedException("No tiene permiso para editar el usuario");
        }

        mapper.updateEntityFromDto(dto, usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Authentication authentication) {

        var userAuthenticated = authenticationValidator.getAuthenticationInstance(authentication);
        if (userAuthenticated instanceof Usuario localUser) {
            Usuario usuario = usuarioRepository.findById(localUser.getId()).orElseThrow(
                    () -> new EntityNotFoundException("No existe el usuario")
            );
            usuario.setActivo(false);
            return;
        }

        if (userAuthenticated instanceof GoogleUser googleUser){
            GoogleUser gUsuario = googleUserRepository.findById(googleUser.getId()).orElseThrow(
                    () -> new EntityNotFoundException("No existe el usuario")
            );
            gUsuario.setActivo(false);
            return;
        }

        throw new IllegalArgumentException("Parámetro de autenticación no válido.");
    }
}
