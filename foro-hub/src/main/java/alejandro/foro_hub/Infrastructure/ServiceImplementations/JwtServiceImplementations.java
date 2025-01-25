package alejandro.foro_hub.Infrastructure.ServiceImplementations;

import alejandro.foro_hub.Application.Services.JwtService;
import alejandro.foro_hub.Domain.Exceptions.TokenInvalidException;
import alejandro.foro_hub.Domain.Exceptions.TokenNullException;
import alejandro.foro_hub.Domain.Models.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtServiceImplementations implements JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Override
    public String obtenerToken(Usuario usuario) {
        return Jwts.builder()
                .issuer("Alejandro")
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nombre", usuario.getNombre())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(3600)))
                .signWith(getKeyEncoded(), Jwts.SIG.HS512)
                .compact();
    }

    @Override
    public Boolean tokenValido(String token) {
        Claims claims;
        try{
            claims = Jwts.parser()
                    .verifyWith(getKeyEncoded())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String obtenerSujeto(String token) {
        if(token == null || token.isEmpty()) throw new TokenNullException("El token es vacío o nulo.");
        try{
            return Jwts.parser()
                    .verifyWith(getKeyEncoded())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            throw new TokenInvalidException("No se ha obtenido el sujeto debido a token no válido.");
        }
    }

    private SecretKey getKeyEncoded(){
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }
}
