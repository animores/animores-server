package animores.serverapi.to_do.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.pet.entity.Pet;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoPageResponse;

import java.util.List;

public interface ToDoService {
    void createToDo(Account account, ToDoCreateRequest request);

    ToDoPageResponse getTodayToDo(Boolean done, List<Pet> pets, Integer page, Integer size);

    ToDoPageResponse getAllToDo(Boolean done, List<Pet> pets, Integer page, Integer size);

    ToDoDetailResponse getToDoById(Long id, Long accountId);

    ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request, Long accountId);

    void deleteToDoById(Long id, Long profileId);

    void checkToDo(Long toDoId, Long accountId);
}
