package animores.serverapi.account.controller;

import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.response.AccountCreateResponse;
import animores.serverapi.account.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<AccountCreateResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) throws Exception {
        AccountCreateResponse response = accountService.createAccount(request);

        if (response == null) {
            throw new Exception();
        }

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Boolean> isDuplicatedEmail(@PathVariable @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$") String email) {
        return ResponseEntity.ok().body(accountService.isDuplicatedEmail(email));
    }

    @GetMapping("/check-nickname/{nickname}")
    public ResponseEntity<Boolean> isDuplicatedNickname(@PathVariable @NotBlank String nickname) {
        return ResponseEntity.ok().body(accountService.isDuplicatedNickname(nickname));
    }

}
