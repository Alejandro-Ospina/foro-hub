package alejandro.foro_hub.Application.Services;

import java.util.Map;

public interface JwtGoogleService {

    Boolean isGoogleAccessTokenValid(String accesToken);
    Map<String, Object> getClaims(String idToken);
    Boolean isGoogleIdTokenValid(String idToken);
    Boolean isGoogleUserActive(String idToken);
}
