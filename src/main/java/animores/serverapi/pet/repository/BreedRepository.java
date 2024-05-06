package animores.serverapi.pet.repository;

import animores.serverapi.pet.domain.Breed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreedRepository extends JpaRepository<Breed, Long> {
}
