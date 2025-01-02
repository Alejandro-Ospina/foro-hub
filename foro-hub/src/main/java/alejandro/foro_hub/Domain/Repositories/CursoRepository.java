package alejandro.foro_hub.Domain.Repositories;

import alejandro.foro_hub.Domain.Models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    Optional<Curso> findByNombreIgnoreCase(String nombre);
}
