package alejandro.foro_hub.Domain.Repositories;

import alejandro.foro_hub.Domain.Models.UsuarioBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioBaseRepository extends JpaRepository<UsuarioBase, Long> {
}
