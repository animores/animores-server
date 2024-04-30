package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.ToDoInstance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ToDoInstanceRepository extends JpaRepository<ToDoInstance, Long>, ToDoInstanceCustomRepository{
    @EntityGraph(attributePaths = {"toDo"})
    Optional<ToDoInstance> findByToDo_IdAndCompleteProfileIsNull(Long toDoId);
}
