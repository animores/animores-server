package animores.serverapi.to_do.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoPageResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;

import java.util.List;

public interface ToDoService {
    void createToDo(Account account, ToDoCreateRequest request);

    ToDoPageResponse getTodayToDo(Boolean done, List<Pet> pets, Integer page, Integer size);

    ToDoPageResponse getAllToDo(Boolean done, List<Pet> pets, Integer page, Integer size);

    ToDoDetailResponse getToDoById(Long id);

    ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request);

    void deleteToDoById(Long id);

    void checkToDo(Long toDoId);
}
