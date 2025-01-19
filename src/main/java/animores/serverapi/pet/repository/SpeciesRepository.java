package animores.serverapi.pet.repository;

import animores.serverapi.pet.entity.Species;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpeciesRepository extends JpaRepository<Species, Long> {

    List<Species> findAll();
}