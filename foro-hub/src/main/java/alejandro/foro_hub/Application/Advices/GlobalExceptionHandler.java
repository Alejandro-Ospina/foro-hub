package alejandro.foro_hub.Application.Advices;

import alejandro.foro_hub.Application.DTOs.ResponseEntityDto;
import alejandro.foro_hub.Domain.Exceptions.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler (EntityExistsException.class)
    public ResponseEntity<?> handleEntityExist(EntityExistsException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler (UsernameNotFoundException.class)
    public ResponseEntity<?> handleUserNameNotFound(UsernameNotFoundException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (TokenNullException.class)
    public ResponseEntity<?> handleTokenNullOrEmpty(TokenNullException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.NO_CONTENT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (TokenInvalidException.class)
    public ResponseEntity<?> handleTokenInvalid(TokenInvalidException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.NO_CONTENT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (CategoriaException.class)
    public ResponseEntity<?> handleCategoriaException(CategoriaException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgument(MethodArgumentNotValidException ex){
        var errores = ex.getFieldErrors().stream().map(ResponseEntityDto::new).toList();
        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler (Exception.class)
    public ResponseEntity<?> handleException(Exception ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getLocalizedMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getLocalizedMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (EntityNotFoundException.class)
    public ResponseEntity<?> handleEntityNotFound(EntityNotFoundException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionException(PermissionDeniedException ex){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler (OAuthStateException.class)
    public ResponseEntity<?> handleOAuthStateException(OAuthStateException e){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                e.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e){
        ResponseEntityDto response = new ResponseEntityDto(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                e.getMessage()
        );
        return ResponseEntity.badRequest().body(response);
    }
}
