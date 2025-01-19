package animores.serverapi.pet.repository;

import animores.serverapi.pet.entity.Breed;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Long> {

    List<Breed> findAllBySpecies_Id(Long speciesId);
}
