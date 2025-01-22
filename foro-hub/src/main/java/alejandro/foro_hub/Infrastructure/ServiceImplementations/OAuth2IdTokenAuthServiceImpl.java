package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.Services.OAuth2IdTokenAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class OAuth2IdTokenAuthServiceImpl implements OAuth2IdTokenAuthenticationService {

    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomOAuthAuthorizationResolver resolver;
    private final JwtDecoder decoder;

    @Override
    public OAuth2AccessTokenResponse getAccessTokenResponse(HttpServletRequest request, String code, String state){
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        OAuth2AuthorizationRequest authorizationRequest = resolver.resolve(request, "google");
        OAuth2AuthorizationResponse authorizationResponse = OAuth2AuthorizationResponse
                .success(code)
                .state(state)
                .redirectUri(authorizationRequest.getRedirectUri())
                .build();

        OAuth2AuthorizationExchange authorizationExchange =
                new OAuth2AuthorizationExchange(authorizationRequest, authorizationResponse);

        OAuth2AuthorizationCodeGrantRequest grantRequest =
                new OAuth2AuthorizationCodeGrantRequest(clientRegistration, authorizationExchange);

        return accessTokenResponseClient.getTokenResponse(grantRequest);
    }

    @Override
    public void setIdTokenContextAuthentication(String idToken){
        Authentication authentication = new JwtAuthenticationToken(
                decoder.decode(idToken),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
