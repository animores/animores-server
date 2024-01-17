package animores.serverapi.account.controller;

import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.response.AccountCreateResponse;
import animores.serverapi.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreateResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountCreateResponse response = accountService.createAccount(request);

        if (response == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().body(response);
    }

}
