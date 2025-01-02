package alejandro.foro_hub.Application.DTOs;

import alejandro.foro_hub.Domain.Models.Curso;
import alejandro.foro_hub.Domain.Models.Topico;

import java.time.LocalDateTime;
import java.util.List;

public record TopicShowDto(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        Boolean status,
        String autor,
        String email,
        Curso curso,
        List<RespuestaShowDto> respuestas
) {
    public TopicShowDto(Topico topico){
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getNombre(),
                topico.getAutor().getEmail(),
                topico.getCurso(),
                RespuestaShowDto.fromTopico(topico)
        );
    }
}
