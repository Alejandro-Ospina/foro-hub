package alejandro.foro_hub.Application.DTOs;

import alejandro.foro_hub.Domain.Models.Respuesta;
import alejandro.foro_hub.Domain.Models.Topico;

import java.time.LocalDateTime;
import java.util.List;

public record RespuestaShowDto(

        Long id,
        String nombreUsuario,
        String email,
        String mensaje,
        LocalDateTime fechaCreacion,
        String solucion
) {
    public RespuestaShowDto(Respuesta respuesta){
        this(
                respuesta.getId(),
                respuesta.getAutor().getNombre(),
                respuesta.getAutor().getEmail(),
                respuesta.getMensaje(),
                respuesta.getFechaCreacion(),
                respuesta.getSolucion()
        );
    }

    public static List<RespuestaShowDto> fromTopico (Topico topico){
        return topico.getRespuestas().stream()
                .map(respuesta -> new RespuestaShowDto(
                        respuesta.getId(),
                        respuesta.getAutor().getUsername(),
                        respuesta.getAutor().getEmail(),
                        respuesta.getMensaje(),
                        respuesta.getFechaCreacion(),
                        respuesta.getSolucion()
                ))
                .toList();
    }
}
