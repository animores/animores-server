package animores.serverapi.account.controller;


import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.response.AccountCreateResponse;
import animores.serverapi.account.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    public ResponseEntity<AccountCreateResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) throws Exception {
        AccountCreateResponse response = accountService.createAccount(request);

        if (response == null) {
            throw new Exception();
        }

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Long> signIn(@Valid @RequestBody SignInRequest request) throws Exception {
        Long response = accountService.signIn(request);

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
    public ResponseEntity<Boolean> isDuplicatedNickname(@PathVariable @Size(min = 2, max = 8) String nickname) {
        return ResponseEntity.ok().body(accountService.isDuplicatedNickname(nickname));
    }

}
