package animores.serverapi.to_do.dto.response;

import animores.serverapi.to_do.dao.GetToDoPageDao;
import java.util.List;
import java.util.Map;

public record ToDoPageResponse(
    Integer curPage,
    Integer size,
    Integer totalCount,
    Integer totalPage,
    List<ToDoResponse> toDoList
) {

    public static ToDoPageResponse fromGetToDoPageDaoAndToDoPetMap(GetToDoPageDao getToDoPageDao,
        Map<Long, List<PetResponse>> toDoIdPetResponseMap) {
        return new ToDoPageResponse(
            getToDoPageDao.curPage(),
            getToDoPageDao.size(),
            getToDoPageDao.totalCount(),
            getToDoPageDao.totalPage(),
            getToDoPageDao.toDoInstanceList().stream().map(
                toDoInstanceDao -> ToDoResponse.fromToDoInstanceDao(toDoInstanceDao,
                    toDoIdPetResponseMap.get(toDoInstanceDao.toDo().id()))
            ).toList()
        );
    }
}
