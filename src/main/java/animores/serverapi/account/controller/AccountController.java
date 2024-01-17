package animores.serverapi.account.controller;

import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Void> createAccount(@RequestBody AccountCreateRequest request) {
        accountService.createAccount(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
