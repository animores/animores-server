package animores.serverapi.pet.controller;

import animores.serverapi.common.Response;
import animores.serverapi.pet.service.PetBatchService;
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
    public Response<Void> insertPetBatch(@RequestParam Integer count, @RequestParam Integer accountStartId) {
        petBatchService.insertPetBatch(count, accountStartId);
        return Response.success(null);
    }

}
