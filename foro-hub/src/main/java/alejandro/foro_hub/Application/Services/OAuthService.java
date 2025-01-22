package alejandro.foro_hub.Application.Services;

import alejandro.foro_hub.Domain.Exceptions.OAuthStateException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface OAuthService {

    String generateState() throws OAuthStateException;
    Boolean validState(String state) throws NoSuchAlgorithmException, InvalidKeyException, OAuthStateException;
    void saveState(String state);
    void saveIdToken(String uuid, String idToken);
    void deleteIdToken(String uuid);
    String getIdToken(String uuid);
}
