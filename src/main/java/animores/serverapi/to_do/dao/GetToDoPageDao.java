package animores.serverapi.to_do.dao;

import java.util.List;

public record GetToDoPageDao(
    Integer curPage,
    Integer size,
    Integer totalCount,
    Integer totalPage,
    List<ToDoInstanceDao> toDoInstanceList
) {

}
