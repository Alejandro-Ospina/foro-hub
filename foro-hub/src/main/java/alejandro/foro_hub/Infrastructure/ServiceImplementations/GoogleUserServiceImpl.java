package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.DTOs.GoogleUserDto;
import alejandro.foro_hub.Application.Mappers.GoogleUserMapper;
import alejandro.foro_hub.Application.Services.ExternalUserService;
import alejandro.foro_hub.Domain.Models.GoogleUser;
import alejandro.foro_hub.Domain.Repositories.GoogleUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleUserServiceImpl implements ExternalUserService {

    private final GoogleUserRepository repository;
    private final GoogleUserMapper mapper;

    @Override
    public <T> void crearUsuarioExterno(T dto) {
        if (dto instanceof GoogleUserDto googleUserDto){
            repository.findBySub(googleUserDto.sub())
                    .ifPresentOrElse(
                            gUser -> {
                                if (!gUser.getActivo()) {
                                    throw new RuntimeException("Usuario de google no activo en la plataforma.");
                                }
                                actualizarUsuarioExterno(googleUserDto, gUser);
                            },
                            () -> {
                                GoogleUser user = mapper.dtoToEntity(googleUserDto);
                                user.setActivo(true);
                                repository.save(user);
                            }
                    );
            return;
        }

        throw new IllegalArgumentException("Tipo no soportado: " + dto.getClass());
    }

    @Transactional
    private void actualizarUsuarioExterno(GoogleUserDto dto, GoogleUser googleUser){
        mapper.updateEntity(dto, googleUser);
    }
}
