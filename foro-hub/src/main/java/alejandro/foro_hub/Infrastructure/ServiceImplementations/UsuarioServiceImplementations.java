package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.ActualizarUsuarioDto;
import alejandro.foro_hub.Application.DTOs.UsuarioDTO;
import alejandro.foro_hub.Application.Mappers.UsuarioMapper;
import alejandro.foro_hub.Application.Services.UsuarioService;
import alejandro.foro_hub.Application.Validators.Validador;
import alejandro.foro_hub.Domain.Exceptions.PermissionDeniedException;
import alejandro.foro_hub.Domain.Models.Usuario;
import alejandro.foro_hub.Domain.Repositories.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthenticationManager authenticationManager;

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
        Usuario usuarioAutenticado = (Usuario) authentication.getPrincipal();

        Usuario usuario = usuarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("No se encontró un usuario con id: " + id)
        );

        if (!Objects.equals(usuario.getId(), usuarioAutenticado.getId())){
            throw new PermissionDeniedException("No tiene permiso para editar el usuario");
        }

        mapper.updateEntityFromDto(dto, usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Authentication authentication) {
        Usuario usuario = usuarioRepository.findByNombre(authentication.getName()).orElseThrow(
                () -> new EntityNotFoundException("No se ha encontrado el usuario: " + authentication.getName())
        );

        usuario.setActivo(false);
    }
}
