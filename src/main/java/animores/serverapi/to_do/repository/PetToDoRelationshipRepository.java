package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.PetToDoRelationship;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetToDoRelationshipRepository extends JpaRepository<PetToDoRelationship, Long> {

    List<PetToDoRelationship> findAllByPet_IdIn(List<Long> petIds);

    void deleteAllByToDo_IdAndPet_IdIn(Long toDoId, List<Long> petIdsToDelete);
}