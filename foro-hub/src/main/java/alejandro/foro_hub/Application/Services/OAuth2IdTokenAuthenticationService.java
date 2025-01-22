package alejandro.foro_hub.Application.Services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

public interface OAuth2IdTokenAuthenticationService {

    OAuth2AccessTokenResponse getAccessTokenResponse(HttpServletRequest request, String code, String state);
    void setIdTokenContextAuthentication(String idToken);
}
