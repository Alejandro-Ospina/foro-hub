package alejandro.foro_hub.Application.Validators;

import jakarta.persistence.EntityExistsException;

public interface Validador<T>{

    void validar(T dto) throws EntityExistsException;
}