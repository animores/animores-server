package animores.serverapi.to_do.controller;

import animores.serverapi.common.Response;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class ToDoController {

	private final ToDoService toDoService;

	@PostMapping
	public Response<Void> createToDo(@RequestBody ToDoCreateRequest request) {
		toDoService.createToDo(request);
		return Response.success(null);
	}

	@GetMapping("/today")
	public Response<List<ToDoResponse>> getTodayToDo(@RequestParam(required = false) Boolean done, @RequestParam(required = false) List<Long> pets) {
		return Response.success(toDoService.getTodayToDo(done, pets));
	}

	@GetMapping("")
	public Response<List<ToDoResponse>> getAllToDo(@RequestParam(required = false) Boolean done, @RequestParam(required = false) List<Long> pets) {
		return Response.success(toDoService.getAllToDo(done, pets));
	}

	@GetMapping("/{id}")
	public Response<ToDoDetailResponse> getToDoById(@PathVariable Long id) {
		return Response.success(toDoService.getToDoById(id));
	}

	@PatchMapping("/{id}")
	public Response<ToDoDetailResponse> updateToDoById(@PathVariable Long id, @RequestBody ToDoPatchRequest request) {
		return Response.success(toDoService.updateToDoById(id, request));
	}

	@DeleteMapping("/{id}")
	public Response<Void> deleteToDoById(@PathVariable Long id) {
		toDoService.deleteToDoById(id);
		return Response.success(null);
	}
}
