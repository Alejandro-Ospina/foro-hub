package alejandro.foro_hub.Domain.Exceptions;

public class TokenNullException extends RuntimeException{
    public TokenNullException() {
        super();
    }

    public TokenNullException(String message) {
        super(message);
    }
}
