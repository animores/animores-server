package animores.serverapi.pet.controller;

import animores.serverapi.account.aop.UserInfo;
import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final AccountService accountService;
    private final PetService petService;

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @UserInfo
    @PostMapping("")
    @Operation(summary = "펫 생성", description = "펫을 생성합니다.")
    public Response<PetCreateResponse> createPet(@RequestBody PetCreateRequest request) {
        Account account = accountService.getAccountFromContext();
        return Response.success(petService.createPet(account, request));
    }
}
