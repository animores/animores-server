package animores.serverapi.pet.controller;

import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.account.domain.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.pet.dto.PetDto;
import animores.serverapi.pet.dto.request.PetCreateRequest;
import animores.serverapi.pet.dto.response.BreedResponse;
import animores.serverapi.pet.dto.response.PetCreateResponse;
import animores.serverapi.pet.dto.response.SpeciesResponse;
import animores.serverapi.pet.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pets")
public class PetController {

    private final AccountService accountService;
    private final PetService petService;

    @GetMapping("/species")
    @Operation(summary = "종 조회", description = "종을 조회합니다.")
    public Response<List<SpeciesResponse>> getSpecies() {
        return Response.success(petService.getSpecies());
    }

    @GetMapping("/breeds")
    @Operation(summary = "품종 조회", description = "해당 종의 품종을 조회합니다.")
    public Response<List<BreedResponse>> getBreedsOfSpecies(@RequestParam Long speciesId) {
        return Response.success(petService.getBreedsOfSpecies(speciesId));
    }


    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @UserInfo
    @PostMapping("")
    @Operation(summary = "펫 생성", description = "펫을 생성합니다.")
    public Response<PetCreateResponse> createPet(@RequestBody PetCreateRequest request) {
        Account account = accountService.getAccountFromContext();
        return Response.success(petService.createPet(account, request));
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @UserInfo
    @GetMapping("")
    @Operation(summary = "펫 List", description = "해당 계정의 펫을 가져옵니다.")
    public Response<List<PetDto>> getPets() {
        Account account = accountService.getAccountFromContext();
        return Response.success(petService.getPets(account));
    }
}
