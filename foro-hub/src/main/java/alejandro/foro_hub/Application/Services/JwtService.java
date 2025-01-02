package alejandro.foro_hub.Application.Services;


import alejandro.foro_hub.Domain.Models.Usuario;

public interface JwtService {

    String obtenerToken(Usuario usuario);
    Boolean tokenValido(String token);
    String obtenerSujeto(String token);
}
