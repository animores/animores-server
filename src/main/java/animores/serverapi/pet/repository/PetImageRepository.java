package animores.serverapi.pet.repository;

import animores.serverapi.pet.entity.PetImage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetImageRepository extends JpaRepository<PetImage, Long> {

    List<PetImage> findAllBySpeciesId(Long speciesId);
}