package alejandro.foro_hub.Infrastructure.Adapters.Inputs;

import alejandro.foro_hub.Application.DTOs.GoogleUserDto;
import alejandro.foro_hub.Application.DTOs.TokenDTO;
import alejandro.foro_hub.Application.Services.ExternalUserService;
import alejandro.foro_hub.Application.Services.JwtGoogleService;
import alejandro.foro_hub.Application.Services.OAuth2IdTokenAuthenticationService;
import alejandro.foro_hub.Application.Services.OAuthService;
import alejandro.foro_hub.Domain.Exceptions.OAuthStateException;
import alejandro.foro_hub.Domain.Exceptions.TokenInvalidException;
import alejandro.foro_hub.Domain.Models.GoogleUser;
import alejandro.foro_hub.Infrastructure.ServiceImplementations.CustomOAuthAuthorizationResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
public class OAuthController {

    private final OAuthService oAuthService;
    private final CustomOAuthAuthorizationResolver resolver;
    private final JwtGoogleService jwtService;
    private final OAuth2IdTokenAuthenticationService oAuth2IdTokenAuthenticationService;
    private final ExternalUserService externalUserService;

    @GetMapping("/login")
    public ResponseEntity<?> validateGoogleUser(HttpServletRequest request) throws OAuthStateException {
        OAuth2AuthorizationRequest authorization = resolver.resolve(request, "google");

        if (authorization == null)
            throw new OAuthStateException("No se ha asignado una autorización.");

        String authorizationUrl = UriComponentsBuilder
                .fromUri(URI.create(authorization.getAuthorizationUri()))
                .queryParam("client_id", authorization.getClientId())
                .queryParam("redirect_uri", authorization.getRedirectUri())
                .queryParam("response_type", authorization.getResponseType().getValue())
                .queryParam("scope", String.join("+", authorization.getScopes()))
                .queryParam("state", authorization.getState())
                .build()
                .toUriString();

        oAuthService.saveState(authorization.getState());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(authorizationUrl))
                .build();
    }

    @GetMapping("/user/authorize")
    public ResponseEntity<?> redirectResponse(@RequestParam("state") @NotEmpty String state,
                                              @RequestParam("code") @NotEmpty String code,
                                              HttpServletRequest request) throws OAuthStateException, NoSuchAlgorithmException, InvalidKeyException {
        if (!oAuthService.validState(state))
            throw new OAuthStateException("Estado de redirección inválido.");

        String identificador = UUID.randomUUID().toString();
        var tokenResponse = oAuth2IdTokenAuthenticationService.getAccessTokenResponse(request, code, state);

        if (!jwtService.isGoogleAccessTokenValid(tokenResponse.getAccessToken().getTokenValue()))
            throw new TokenInvalidException("Access token inválido.");

        oAuthService.saveIdToken(
                identificador,
                (String) tokenResponse.getAdditionalParameters().get("id_token"));

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:8080/oauth/register?key=" + identificador))
                .build();
    }

    @GetMapping("/register")
    public ResponseEntity<?> loginGoogleUser(@RequestParam("key") @NotNull String key){
        String idToken = oAuthService.getIdToken(key);
        if (!jwtService.isGoogleIdTokenValid(idToken))
            throw new TokenInvalidException("Token de google inválido.");

        oAuthService.deleteIdToken(key);
        oAuth2IdTokenAuthenticationService.setIdTokenContextAuthentication(idToken);

        String nameResponse = (String) jwtService.getClaims(idToken).get("name");
        externalUserService.crearUsuarioExterno(buildGoogleUserDto(jwtService.getClaims(idToken)));

        return ResponseEntity.ok(new TokenDTO(nameResponse, idToken));
    }

    private GoogleUserDto buildGoogleUserDto(Map<String, Object> claims){
        return new GoogleUserDto(
                (String) claims.get("sub"),
                (String) claims.get("name"),
                (String) claims.get("email"),
                (String) claims.get("picture")
        );
    }
}
