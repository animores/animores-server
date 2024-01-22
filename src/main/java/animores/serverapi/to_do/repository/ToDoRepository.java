package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    Object findAllByDate(Object any);

    Object findAllByAccountId();
}
