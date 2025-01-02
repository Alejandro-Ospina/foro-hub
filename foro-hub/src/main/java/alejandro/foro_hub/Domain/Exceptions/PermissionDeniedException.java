package alejandro.foro_hub.Domain.Exceptions;

public class PermissionDeniedException extends Exception{

    public PermissionDeniedException() {
        super();
    }

    public PermissionDeniedException(String message) {
        super(message);
    }
}
