package animores.serverapi.account.controller;

import animores.serverapi.account.service.AccountBatchService;
import animores.serverapi.common.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account/batch")
public class AccountBatchController {

    private final AccountBatchService accountBatchService;

    @PostMapping("/")
    @Operation(summary = "계정 배치 생성", description = "계정을 배치로 생성합니다.")
    public Response<Void> insertAccountBatch(@RequestParam
                                             @Parameter(description = "배치 개수", required = true, example = "100")
                                             Integer count) {
        accountBatchService.insertAccountBatch(count);
        return Response.success(null);
    }
}
