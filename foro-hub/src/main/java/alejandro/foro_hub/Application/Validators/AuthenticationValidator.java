package alejandro.foro_hub.Application.Validators;

import org.springframework.security.core.Authentication;

public interface AuthenticationValidator<T> {

    T getAuthenticationInstance(Authentication authentication);
}
