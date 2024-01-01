package animores.serverapi.to_do.controller;

import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class ToDoController {

	private final ToDoService toDoService;

	@PostMapping
	public ResponseEntity<Void> createToDo(@RequestBody ToDoCreateRequest request) {
		toDoService.createToDo(request);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/today")
	public ResponseEntity<List<ToDoResponse>> getTodayToDo(@RequestParam Boolean done, @RequestParam List<Long> pets) {
		//TODO: 펫 리스트를 받아 해당 펫 리스트가 그 유저의 펫인지 확인 필요

		return ResponseEntity.ok().body(toDoService.getTodayToDo(done, pets));
	}

	@GetMapping("/all")
	public ResponseEntity<List<ToDoResponse>> getAllToDo(@RequestParam Boolean done, @RequestParam List<Long> pets) {
		//TODO: 펫 리스트를 받아 해당 펫 리스트가 그 유저의 펫인지 확인 필요

		return ResponseEntity.ok().body(toDoService.getAllToDo(done, pets));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ToDoDetailResponse> getToDoById(@PathVariable Long id) {
		return ResponseEntity.ok().body(toDoService.getToDoById(id));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<ToDoDetailResponse> updateToDoById(@PathVariable Long id, @RequestBody ToDoPatchRequest request) {
		return ResponseEntity.ok().body(toDoService.updateToDoById(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteToDoById(@PathVariable Long id) {
		toDoService.deleteToDoById(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
