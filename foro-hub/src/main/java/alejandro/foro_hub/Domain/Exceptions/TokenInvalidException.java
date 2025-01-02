package alejandro.foro_hub.Domain.Exceptions;

public class TokenInvalidException extends RuntimeException{
    public TokenInvalidException() {
        super();
    }

    public TokenInvalidException(String message) {
        super(message);
    }
}
