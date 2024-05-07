package animores.serverapi.pet.repository;

import animores.serverapi.pet.domain.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {
    List<Species> findAll();
}
