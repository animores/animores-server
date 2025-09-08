package animores.serverapi.diary.repository;

import animores.serverapi.to_do.dao.GetTodosDao;
import java.time.LocalDate;
import java.util.List;

public interface TodoCustomRepository {

    List<GetTodosDao> getTodos(Boolean completed, LocalDate start, LocalDate end, Integer page,
        Integer size);

}
