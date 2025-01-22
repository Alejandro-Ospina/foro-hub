package alejandro.foro_hub.Infrastructure.Security;

import alejandro.foro_hub.Application.DTOs.ResponseEntityDto;
import alejandro.foro_hub.Application.Services.OAuthService;
import alejandro.foro_hub.Infrastructure.Filter.AuthenticationFilter;
import alejandro.foro_hub.Infrastructure.ServiceImplementations.CustomOAuthAuthorizationResolver;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig{

    private final AuthenticationFilter filter;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final OAuthService oAuthService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        autorize -> autorize
                                .requestMatchers("/usuarios/registrar", "/usuarios/login").permitAll()
                                .requestMatchers(
                                        "/foro-hub.html",
                                        "/v1/foro-hub/**",
                                        "/swagger-ui/**",
                                        "/api/docs/**",
                                        "/oauth/**"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .headers(
                        header -> header
                                .xssProtection(Customizer.withDefaults())
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                                .contentSecurityPolicy(securityPolicy -> securityPolicy
                                        .policyDirectives("default-src 'self'"))
                )
                .sessionManagement(
                        session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        oAuthConfiguration(http);
        configureExceptionHandling(http);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private void configureExceptionHandling(HttpSecurity http) throws Exception{
        http.exceptionHandling(
                exceptionHandler -> {
                    exceptionHandler
                            .accessDeniedHandler(
                                    (request, response, handler) -> {
                                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                        response.setContentType("application/json");
                                        response.getWriter().write(interceptorSecurityExceptionMessage(
                                                response.getStatus(),
                                                "Acceso denegado. No tiene permisos de acceso a este recurso."
                                        ));
                                    }
                            )
                            .authenticationEntryPoint((request, response, authException) -> {
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                response.setContentType("application/json");
                                response.getWriter().write(interceptorSecurityExceptionMessage(
                                        response.getStatus(),
                                        "Usuario no autenticado o activo. Verifique si está activo o " +
                                                "inicie sesión nuevamente."
                                ));
                            });
                }
        );
    }

    private String interceptorSecurityExceptionMessage(Integer status, String message) throws JsonProcessingException {
        return new ObjectMapper()
                .registerModules(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .writeValueAsString(new ResponseEntityDto(LocalDateTime.now(), status, message));
    }

    private void oAuthConfiguration(HttpSecurity http) throws Exception {
        http.oauth2Login(
                oauth ->
                        oauth.authorizationEndpoint(autorization ->
                                        autorization.authorizationRequestResolver(
                                                customOAuthAuthorizationResolver(
                                                        clientRegistrationRepository,
                                                        oAuthService
                                                )
                                        ))
                                .redirectionEndpoint(redirect ->
                                        redirect.baseUri("/oauth/user/authorize/*"))
                                .clientRegistrationRepository(clientRegistrationRepository)
        );
    }

    @Bean
    public CustomOAuthAuthorizationResolver customOAuthAuthorizationResolver(
            ClientRegistrationRepository repository,
            OAuthService oAuthService){
        return new CustomOAuthAuthorizationResolver(
                clientRegistrationRepository,
                "/oauth/login",
                oAuthService
        );
    }

}
