package alejandro.foro_hub.Application.DTOs;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;

public record ResponseEntityDto(LocalDateTime timestamp, int status, String message) {

    public ResponseEntityDto (FieldError error){
        this(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                error.getField() + ": " + error.getDefaultMessage()
        );
    }
}
