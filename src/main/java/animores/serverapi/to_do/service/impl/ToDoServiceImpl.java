package animores.serverapi.to_do.service.impl;

import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.service.ToDoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {
	@Override
	public void createToDo(ToDoCreateRequest request) {

	}

	@Override
	public List<ToDoResponse> getTodayToDo(Boolean done, List<Long> pets) {
		return null;
	}

	@Override
	public List<ToDoResponse> getAllToDo(Boolean done, List<Long> pets) {
		return null;
	}

	@Override
	public ToDoDetailResponse getToDoById(Long id) {
		return null;
	}

	@Override
	public ToDoDetailResponse updateToDoById(Long id, ToDoPatchRequest request) {
		return null;
	}

	@Override
	public void deleteToDoById(Long id) {

	}
}
