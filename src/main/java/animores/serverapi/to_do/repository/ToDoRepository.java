package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.ToDo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToDoRepository extends JpaRepository<ToDo, Long> {

    List<ToDo> findAllByDate(LocalDate date);

    List<ToDo> findAllByAccountId(Long accountId);

    Optional<ToDo> findByIdAndAccount_Id(Long id, Long accountId);
}