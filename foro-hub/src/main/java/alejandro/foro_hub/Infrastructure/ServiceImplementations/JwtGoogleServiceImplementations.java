package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.Services.JwtGoogleService;
import alejandro.foro_hub.Domain.Exceptions.TokenNullException;
import alejandro.foro_hub.Domain.Repositories.GoogleUserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtGoogleServiceImplementations implements JwtGoogleService {

    @Value("${base.uri.validate.token}")
    private String baseUriValidation;

    @Value("${client.id}")
    private String clientId;

    private final RestTemplate restTemplate;
    private final JwtDecoder decoder;
    private final GoogleUserRepository googleUserRepository;

    @Override
    public Boolean isGoogleAccessTokenValid(String accesToken) {
        try{
            String url = baseUriValidation +
                    "=" +
                    accesToken;

            var response = restTemplate.getForObject(url, Map.class);
            if(response == null || response.isEmpty())
                return false;

            String audience = (String) response.get("aud");
            String emailValid = (String) response.get("email_verified");
            long exp = Long.parseLong((String) response.get("exp"));
            Date expiracion = new Date(exp * 1000);

            return clientId.equals(audience) && emailValid.equals("true") && expiracion.after(new Date());
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public Map<String, Object> getClaims(String idToken) {
        try{
            var claims = decoder.decode(idToken).getClaims();
            if(claims == null || claims.isEmpty())
                throw new TokenNullException("No se han obtenido los claims. Respuesta vacía.");
            return claims;
        } catch (RuntimeException e) {
            throw new TokenNullException("Algo ha ocurrido con el id token de google.");
        }
    }

    @Override
    public Boolean isGoogleIdTokenValid(String idToken) {
        try{
            var claims = decoder
                    .decode(idToken)
                    .getClaims();

            var exp = Date.from((Instant) claims.get("exp")).toInstant();
            var aud = (String) claims.get("azp");
            var emailValid = (boolean) claims.get("email_verified");

            return  clientId.equals(aud) && emailValid && exp.isAfter(new Date().toInstant());
        } catch (JwtException | NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Boolean isGoogleUserActive(String idToken) {
        try{
            var claims = decoder
                    .decode(idToken)
                    .getClaims();

            var sub = (String) claims.get("sub");
            var googleUser = googleUserRepository.findBySub(sub).orElseThrow(
                    () -> new EntityNotFoundException("Error validando usuario de google.")
            );

            return googleUser.getActivo();
        } catch (JwtException | NumberFormatException e) {
            return false;
        }
    }
}
