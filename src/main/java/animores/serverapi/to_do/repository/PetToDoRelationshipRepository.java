package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.PetToDoRelationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetToDoRelationshipRepository extends JpaRepository<PetToDoRelationship, Long> {
	List<PetToDoRelationship> findAllByPet_IdIn(List<Long> petIds);
    void deleteAllByToDo_IdAndPet_IdIn(Long toDoId, List<Long> petIdsToDelete);
    List<PetToDoRelationship> findAllByToDo_IdIn(List<Long> toDoIdList);
}
