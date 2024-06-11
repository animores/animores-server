package animores.serverapi.account.controller;

import animores.serverapi.account.service.AccountBatchService;
import animores.serverapi.common.Response;
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
    public Response<Void> insertAccountBatch(@RequestParam Integer count) {
        accountBatchService.insertAccountBatch(count);
        return Response.success(null);
    }
}
