package alejandro.foro_hub.Application.Validators;

import alejandro.foro_hub.Domain.Models.Usuario;
import alejandro.foro_hub.Domain.Repositories.GoogleUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ValidateUserNamePassAuthentication implements AuthenticationValidator<Object> {

    private final JwtDecoder decoder;
    private final GoogleUserRepository googleUserRepository;

    @Override
    public Object getAuthenticationInstance(Authentication authentication) {
        if (authentication instanceof UsernamePasswordAuthenticationToken){
            return (Usuario) authentication.getPrincipal();
        }

        if (authentication instanceof JwtAuthenticationToken){
            var claims = (Jwt) authentication.getPrincipal();
            return googleUserRepository
                    .findBySub((String) claims.getClaims().get("sub"))
                    .orElseThrow(
                            () -> new EntityNotFoundException("No se encontró sub.")
                    );
        }

        throw new EntityNotFoundException("No se ha encontrado usuario autenticado");
    }
}
