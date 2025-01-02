package alejandro.foro_hub.Application.Controllers;

import org.springframework.http.ResponseEntity;

public interface BaseController<T, ID> {

    ResponseEntity<?> createRegister(T dto);
    ResponseEntity<?> deleteRegister (ID id);
    ResponseEntity<?> updateRegister (T dto, Long id);
}
