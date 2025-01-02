package alejandro.foro_hub.Infrastructure.Filter;

import alejandro.foro_hub.Application.DTOs.ResponseEntityDto;
import alejandro.foro_hub.Application.Services.JwtService;
import alejandro.foro_hub.Domain.Repositories.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements Filter {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        var header = request.getHeader("Authorization");
        if (header != null){

            var token = header.replace("Bearer ", "");
            if (!jwtService.tokenValido(token)){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        new ObjectMapper()
                                .registerModules(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .writeValueAsString(
                                new ResponseEntityDto(
                                        LocalDateTime.now(),
                                        HttpStatus.UNAUTHORIZED.value(),
                                        "Sesión expirada, o credenciales inválidas."
                                )
                        )
                );
                return;
            }

            var email = jwtService.obtenerSujeto(token);
            var usuario = usuarioRepository.findByEmail(email).orElse(null);
            if (usuario != null && !usuario.getActivo()){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write(
                        new ObjectMapper()
                                .registerModules(new JavaTimeModule())
                                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                                .writeValueAsString(
                                new ResponseEntityDto(
                                        LocalDateTime.now(),
                                        HttpStatus.UNAUTHORIZED.value(),
                                        "Usuario inactivo. No tiene permisos"
                                )
                        )
                );
                return;
            }

            if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
