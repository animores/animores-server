package animores.serverapi.profile.controller;

import animores.serverapi.profile.service.ProfileBatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile/batch")
public class ProfileBatchController {

    private final ProfileBatchService profileBatchService;

    @PostMapping("/")
    @Operation(summary = "프로필 배치 생성", description = "프로필을 배치로 생성합니다. 각 계정당 3개의 프로필이 생성됩니다.")
    public void insertProfileBatch(@RequestParam
    @Parameter(description = "배치 개수", required = true, example = "100")
    Integer count,
        @RequestParam
        @Parameter(description = "프로필을 생성할 계정 id 의 최솟값입니다. 이 변수 이상의 id 의 계정들에 프로필이 생성됩니다.", required = true, example = "1")
        Integer accountStartId) {
        profileBatchService.insertProfileBatch(count, accountStartId);
    }
}
