package animores.serverapi.pet.repository;

import animores.serverapi.pet.entity.PetImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetImageRepository extends JpaRepository<PetImage, Long> {
}
