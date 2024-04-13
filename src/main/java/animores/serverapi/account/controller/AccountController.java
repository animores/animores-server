package animores.serverapi.account.controller;


import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.request.SignOutRequest;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.config.security.RefreshRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/refresh")
    public Response<SignInResponse> refresh(@Valid @RequestBody RefreshRequest request) throws Exception {
        SignInResponse response = accountService.refresh(request);

        if (response == null) {
            throw new Exception();
        }

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        SignUpResponse response = accountService.signUp(request);

        if (response == null) {
            //TODO: 에러코드 추가
            throw new CustomException(ExceptionCode.UNHANDLED_EXCEPTION);
        }

        return Response.success(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) throws Exception {
        SignInResponse response = accountService.signIn(request);

        if (response == null) {
            throw new Exception();
        }

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/sign-out")
    @PreAuthorize("hasAuthority('USER')")
    public void signOut(@Valid @RequestBody SignOutRequest request, @AuthenticationPrincipal User user) {
        accountService.signOut(request, user);
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
