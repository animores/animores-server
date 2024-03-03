package animores.serverapi.account.controller;


import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.response.AccountCreateResponse;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Response<AccountCreateResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) throws Exception {
        AccountCreateResponse response = accountService.createAccount(request);

        if (response == null) {
            //TODO: 에러코드 추가
            throw new CustomException(ExceptionCode.UNHANDLED_EXCEPTION);
        }

        return Response.success(response);
    }

    @GetMapping("/check-email/{email}")
    public Response<Boolean> isDuplicatedEmail(@PathVariable @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$") String email) {
        return Response.success(accountService.isDuplicatedEmail(email));
    }

    @GetMapping("/check-nickname/{nickname}")
    public Response<Boolean> isDuplicatedNickname(@PathVariable @Size(min = 2, max = 8) String nickname) {
        return Response.success(accountService.isDuplicatedNickname(nickname));
    }

}
