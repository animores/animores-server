package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.ToDoInstance;

import java.util.List;

public interface ToDoInstanceCustomRepository {

	List<ToDoInstance> findAllByCompleteFalseAndTodayToDoIdIn(List<Long> toDoIds);
	List<ToDoInstance> findAllByCompleteAndTodayToDoIdIn(List<Long> toDoIds);
	List<ToDoInstance> findAllByCompleteFalseAndToDoIdIn(List<Long> toDoIds);
	List<ToDoInstance> findAllByCompleteAndToDoIdIn(List<Long> toDoIds);
}
