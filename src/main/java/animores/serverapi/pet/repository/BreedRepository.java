package animores.serverapi.pet.repository;

import animores.serverapi.pet.entity.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BreedRepository extends JpaRepository<Breed, Long> {
    List<Breed> findAllBySpecies_Id(Long speciesId);
}
