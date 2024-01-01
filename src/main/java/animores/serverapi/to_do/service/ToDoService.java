package animores.serverapi.to_do.service;

import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;

import java.util.List;

public interface ToDoService {
	public void createToDo(ToDoCreateRequest request);
	public List<ToDoResponse> getTodayToDo(Boolean done, List<Long> pets);
	public List<ToDoResponse> getAllToDo(Boolean done, List<Long> pets);
	public ToDoDetailResponse getToDoById(Long id);
	public ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request);
	public void deleteToDoById(Long id);
}
