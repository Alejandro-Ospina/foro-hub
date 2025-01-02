package alejandro.foro_hub.Domain.Repositories;

import alejandro.foro_hub.Domain.Models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<Usuario> findByNombre(String nombre);
}
