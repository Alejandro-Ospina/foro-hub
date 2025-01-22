package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.Services.OAuthService;
import alejandro.foro_hub.Domain.Exceptions.OAuthStateException;
import alejandro.foro_hub.Domain.Exceptions.TokenInvalidException;
import alejandro.foro_hub.Domain.Exceptions.TokenNullException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OAuthServiceImplementations implements OAuthService {

    @Value("${state.secret}")
    private String secretStateString;

    @Value("${redis.state.duration}")
    private Long duracionEstadoRedis;

    @Value("${client.secret}")
    private String clientSecret;

    private final SecureRandom secureRandom;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String generateState() throws OAuthStateException {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String randomString = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        String signature = signStateString(randomString);
        return randomString.concat(".").concat(signature);
    }

    @Override
    public Boolean validState(String state) throws OAuthStateException {
        String[] stateParts = state.split("\\.");
        if(stateParts.length != 2) return false;

        String stateRandom = stateParts[0];
        String stateRandomSigned = stateParts[1];

        String signedRandom = signStateString(stateRandom);
        if(signedRandom.equals(stateRandomSigned) && redisTemplate.hasKey(state)){
            redisTemplate.delete(state);
            return true;
        }

        return false;
    }

    @Override
    public void saveState(String state) {
        redisTemplate.opsForValue().set(state, "valid", duracionEstadoRedis, TimeUnit.SECONDS);
    }

    @Override
    public void saveIdToken(String uuid, String idToken) {
        if (redisTemplate.hasKey(uuid))
            throw new TokenInvalidException("Identificador no aceptado.");

        redisTemplate.opsForValue().set(uuid, idToken, duracionEstadoRedis, TimeUnit.SECONDS);
    }

    @Override
    public void deleteIdToken(String uuid) {
        if (!redisTemplate.hasKey(uuid))
            throw new TokenInvalidException("Identificador no reconocido.");

        redisTemplate.delete(uuid);
    }

    @Override
    public String getIdToken(String uuid) {
        if (uuid == null)
            throw new TokenNullException("Identificador vacío o nulo.");

        return redisTemplate.opsForValue().get(uuid);
    }

    private String signStateString(String data) throws OAuthStateException {
        if (secretStateString.length() < 32)
            throw new IllegalArgumentException("La clave debe contar con una longitud superior o igual a 32.");

        SecretKey secretKey = Keys.hmacShaKeyFor(secretStateString.getBytes(StandardCharsets.UTF_8));
        Mac mac = null;
        try {
            mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            byte[] bytes = mac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new OAuthStateException("Error generando firma.", e.getMessage());
        }
    }
}
