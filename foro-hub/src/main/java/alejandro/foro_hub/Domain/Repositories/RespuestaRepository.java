package alejandro.foro_hub.Domain.Repositories;

import alejandro.foro_hub.Domain.Models.Respuesta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

    @Query("""
            SELECT r
            FROM Respuesta r
            WHERE r.autor.email = :email
            """)
    Page<Respuesta> userAnswersList(@Param("email") String email, Pageable pageable);
}
