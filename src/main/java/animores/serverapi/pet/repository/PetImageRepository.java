package animores.serverapi.pet.repository;

import animores.serverapi.pet.entity.PetImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetImageRepository extends JpaRepository<PetImage, Long> {
    List<PetImage> findAllBySpeciesId(Long speciesId);
}
