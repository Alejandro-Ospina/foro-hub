package alejandro.foro_hub.Infrastructure.ServiceImplementations;


import alejandro.foro_hub.Application.Services.OAuthService;
import alejandro.foro_hub.Domain.Exceptions.OAuthStateException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuthAuthorizationResolver implements OAuth2AuthorizationRequestResolver {

    private final OAuth2AuthorizationRequestResolver defaultResolver;
    private final OAuthService oAuthService;

    public CustomOAuthAuthorizationResolver(ClientRegistrationRepository repository, String authorizationBaseUri, OAuthService oAuthService) {
        this.oAuthService = oAuthService;
        this.defaultResolver =
                new DefaultOAuth2AuthorizationRequestResolver(repository, authorizationBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request){
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
        if(req != null) {
            try {
                req = authorizationRequest(req);
            } catch (OAuthStateException e) {
                throw new RuntimeException(e);
            }
        }

        return req;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest req = defaultResolver.resolve(request, clientRegistrationId);
        if(req != null) {
            try {
                req = authorizationRequest(req);
            } catch (OAuthStateException e) {
                throw new RuntimeException(e);
            }
        }
        return req;
    }

    private OAuth2AuthorizationRequest authorizationRequest(OAuth2AuthorizationRequest request) throws OAuthStateException {
        String state = oAuthService.generateState();
        return OAuth2AuthorizationRequest.from(request).state(state).build();
    }
}
