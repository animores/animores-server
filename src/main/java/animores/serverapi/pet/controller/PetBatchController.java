package animores.serverapi.pet.controller;

import animores.serverapi.common.Response;
import animores.serverapi.pet.service.PetBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/pets/batch")
@RestController
public class PetBatchController {

    private final PetBatchService petBatchService;

    @PostMapping("/")
    @Operation(summary = "반려동물 배치 생성", description = "반려동물을 배치로 생성합니다. 각 계정당 3개의 반려동물이 생성됩니다.")
    public Response<Void> insertPetBatch(@RequestParam
    @Parameter(description = "배치 개수", required = true, example = "100")
    Integer count,
        @RequestParam
        @Parameter(description = "반려동물을 생성할 계정 id 의 최솟값입니다. 이 변수 이상의 id 의 계정들에 반려동물이 생성됩니다.", required = true, example = "1")
        Integer accountStartId) {
        petBatchService.insertPetBatch(count, accountStartId);
        return Response.success(null);
    }

}
