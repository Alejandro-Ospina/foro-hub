package alejandro.foro_hub.Domain.Repositories;

import alejandro.foro_hub.Domain.Models.Topico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Optional<Topico> findByTituloAndMensajeIgnoreCase(String titulo, String mensaje);

    @Query("""
            SELECT t
            FROM Topico t
            JOIN FETCH t.autor
            JOIN FETCH t.curso
            WHERE t.autor.activo = TRUE
            """)
    List<Topico> findAllTopicsWithDetails();

    @Query("""
            SELECT t
            FROM Topico t
            JOIN FETCH t.autor
            JOIN FETCH t.curso
            WHERE t.id = :id
            """)
    Optional<Topico> findTopicByIdWithDetails(Long id);

    Optional<Topico> findByTituloIgnoreCase(String titulo);
}
