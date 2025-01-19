package animores.serverapi.profile.repository;

import animores.serverapi.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    List<Profile> findAllByAccountIdAndDeletedAtIsNull(Long id);

    Optional<Profile> findByIdAndAccountIdAndDeletedAtIsNull(Long id, Long accountId);

    Integer countByAccountIdAndDeletedAtIsNull(Long accountId);

    Optional<Profile> findByIdAndDeletedAtIsNull(Long profileId);
}
