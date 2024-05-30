package animores.serverapi.to_do.controller;

import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.pet.entity.Pet;
import animores.serverapi.pet.service.PetService;
import animores.serverapi.to_do.dto.request.ToDoCreateRequest;
import animores.serverapi.to_do.dto.request.ToDoPatchRequest;
import animores.serverapi.to_do.dto.response.ToDoDetailResponse;
import animores.serverapi.to_do.dto.response.ToDoPageResponse;
import animores.serverapi.to_do.service.ToDoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@SecurityRequirement(name = "Authorization")
//@PreAuthorize("hasRole('USER')")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
public class ToDoController {

	private final AccountService accountService;
	private final PetService petService;
	private final ToDoService toDoService;

	@Operation(summary = "To Do 생성", description = "To Do를 생성합니다.")
	@PostMapping
	@UserInfo
	public Response<Void> createToDo(@RequestBody ToDoCreateRequest request) {
		Account account = accountService.getAccountFromContext();
		petService.checkAccountPets(account.getId(), request.petIds());
		toDoService.createToDo(account, request);
		return Response.success(null);
	}

	@Operation(summary = "오늘의 To Do 조회", description = "오늘의 To Do를 조회합니다.")
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

	@Operation(summary = "To Do 전체 조회", description = "To Do를 전체 조회합니다.")
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

	@Operation(summary = "To Do 상세 조회", description = "To Do id를 입력받아 To Do를 상세 조회합니다.")
	@GetMapping("/{id}")
	@UserInfo
	public Response<ToDoDetailResponse> getToDoById(@PathVariable Long id) {
		Account account = accountService.getAccountFromContext();
		return Response.success(toDoService.getToDoById(id, account.getId()));
	}

	@Operation(summary = "To Do 수정", description = "To Do id를 입력받아 To Do를 수정합니다.")
	@PatchMapping("/{id}")
	@UserInfo
	public Response<ToDoDetailResponse> updateToDoById(@PathVariable Long id, @RequestBody ToDoPatchRequest request) {
		Account account = accountService.getAccountFromContext();
		return Response.success(toDoService.updateToDoById(id, request, account.getId()));
	}

	@Operation(summary = "To Do 삭제", description = "To Do id를 입력받아 To Do를 삭제합니다.")
	@DeleteMapping("/{id}")
	@UserInfo
	public Response<Void> deleteToDoById(@PathVariable Long id, @PathVariable Long profileId) {
		toDoService.deleteToDoById(id, profileId);
		return Response.success(null);
	}

	@Operation(summary = "To Do 체크", description = "To Do id를 입력받아 To Do를 체크합니다.")
	@PostMapping("/{id}/check")
	@UserInfo
	public Response<Void> checkToDo(@PathVariable Long id) {
		Account account = accountService.getAccountFromContext();
		toDoService.checkToDo(id, account.getId());
		return Response.success(null);
	}
}
