package alejandro.foro_hub.Domain.Repositories;

import alejandro.foro_hub.Domain.Models.GoogleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {

    Optional<GoogleUser> findBySub(String sub);
}
