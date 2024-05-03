package animores.serverapi.to_do.controller;

import animores.serverapi.account.aop.UserInfo;
import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.pet.domain.Pet;
import animores.serverapi.pet.service.PetService;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoPageResponse;
import animores.serverapi.to_do.dto.response.ToDoResponse;
import animores.serverapi.to_do.service.ToDoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "Authorization")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class ToDoController {

	private final AccountService accountService;
	private final PetService petService;
	private final ToDoService toDoService;

	@PostMapping
	@UserInfo
	public Response<Void> createToDo(@RequestBody ToDoCreateRequest request) {
		Account account = accountService.getAccountFromContext();
		petService.checkAccountPets(account.getId(), request.petIds());
		toDoService.createToDo(account, request);
		return Response.success(null);
	}

	@GetMapping("/today")
	@UserInfo
	public Response<ToDoPageResponse> getTodayToDo(@RequestParam(required = false) Boolean done,
												   @RequestParam(required = false) List<Long> pets,
												   @RequestParam Integer page,
												   @RequestParam Integer size) {
		Account account = accountService.getAccountFromContext();
		List<Pet> petList = petService.checkAccountPets(account.getId(), pets);
		return Response.success(toDoService.getTodayToDo(done, petList, page, size));
	}

	@GetMapping("")
	@UserInfo
	public Response<ToDoPageResponse> getAllToDo(@RequestParam(required = false) Boolean done,
												   @RequestParam(required = false) List<Long> pets,
												   @RequestParam Integer page,
												   @RequestParam Integer size) {
		Account account = accountService.getAccountFromContext();
		List<Pet> petList= petService.checkAccountPets(account.getId(), pets);
		return Response.success(toDoService.getAllToDo(done, petList, page, size));
	}

	@GetMapping("/{id}")
	@UserInfo
	public Response<ToDoDetailResponse> getToDoById(@PathVariable Long id) {
		Account account = accountService.getAccountFromContext();
		return Response.success(toDoService.getToDoById(id));
	}

	@PatchMapping("/{id}")
	@UserInfo
	public Response<ToDoDetailResponse> updateToDoById(@PathVariable Long id, @RequestBody ToDoPatchRequest request) {
		Account account = accountService.getAccountFromContext();
		return Response.success(toDoService.updateToDoById(id, request));
	}

	@DeleteMapping("/{id}")
	@UserInfo
	public Response<Void> deleteToDoById(@PathVariable Long id) {
		Account account = accountService.getAccountFromContext();
		toDoService.deleteToDoById(id);
		return Response.success(null);
	}
}
