package animores.serverapi.to_do.repository;

import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;

import java.util.List;

public interface ToDoInstanceCustomRepository {

	List<ToDoInstanceVo> findAllByCompleteFalseAndTodayToDoIdIn(List<Long> toDoIds);
	List<ToDoInstanceVo> findAllByCompleteAndTodayToDoIdIn(List<Long> toDoIds);
	List<ToDoInstanceVo> findAllByCompleteFalseAndToDoIdIn(List<Long> toDoIds);
	List<ToDoInstanceVo> findAllByCompleteAndToDoIdIn(List<Long> toDoIds);
	List<ToDoInstanceVo> findAllByTodayToDoIdIn(List<Long> todoIdList);
	List<ToDoInstanceVo> findAllByToDoIdIn(List<Long> todoIdList);
}
