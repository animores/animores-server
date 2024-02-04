package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.PetToDoRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetToDoRelationshipRepository extends JpaRepository<PetToDoRelationship, Long> {
}
