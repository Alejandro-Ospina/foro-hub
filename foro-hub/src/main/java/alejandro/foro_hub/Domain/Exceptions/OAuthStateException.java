package alejandro.foro_hub.Domain.Exceptions;

public class OAuthStateException extends Exception{

    private String mensaje;

    public OAuthStateException() {
        super();
    }

    public OAuthStateException(String message) {
        super(message);
    }

    public OAuthStateException(String message, String mensaje) {
        super(message);
        this.mensaje = mensaje;
    }
}
