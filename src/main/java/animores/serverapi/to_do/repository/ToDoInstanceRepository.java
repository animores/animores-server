package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.ToDoInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ToDoInstanceRepository extends JpaRepository<ToDoInstance, Long>, ToDoInstanceCustomRepository{
}
