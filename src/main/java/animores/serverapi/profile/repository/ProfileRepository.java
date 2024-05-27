package animores.serverapi.profile.repository;

import animores.serverapi.profile.domain.Profile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    @EntityGraph(attributePaths = {"profileImage"})
    List<Profile> findAllByAccountId(Long id);

    Optional<Profile> findByIdAndAccountId(Long id, Long accountId);
}
