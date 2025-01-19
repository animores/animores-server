package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.ToDoInstance;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoInstanceRepository extends JpaRepository<ToDoInstance, Long>,
    ToDoInstanceCustomRepository {

    @EntityGraph(attributePaths = {"toDo"})
    Optional<ToDoInstance> findByToDo_IdAndCompleteProfileIsNull(Long toDoId);
}
