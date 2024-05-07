package animores.serverapi.account.controller;


import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.request.SignOutRequest;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.account.service.EmailAuthService;
import animores.serverapi.common.Response;
import animores.serverapi.security.RefreshRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Validated
public class AccountController {

    private final AccountService accountService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/refresh")
    public Response<SignInResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return Response.success(accountService.refresh(request));
    }

    @PostMapping("/sign-up")
    public Response<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return Response.success(accountService.signUp(request));
    }

    @PostMapping("/sign-in")
    public Response<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return Response.success(accountService.signIn(request));
    }

    @PostMapping("/sign-out")
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "Authorization")
    public ResponseEntity<Void> signOut(@Valid @RequestBody SignOutRequest request, @AuthenticationPrincipal User user) {
        accountService.signOut(request, user);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/check-email/{email}")
    public Response<Boolean> isDuplicatedEmail(@PathVariable @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$") String email) {
        return Response.success(accountService.isDuplicatedEmail(email));
    }

    @GetMapping("/check-nickname/{nickname}")
    public Response<Boolean> isDuplicatedNickname(@PathVariable @Size(min = 2, max = 8) String nickname) {
        return Response.success(accountService.isDuplicatedNickname(nickname));
    }

    @PostMapping("/email-auth-create")
    public Response<Void> sendAuthEmail(@RequestParam @Email(message = "이메일 형식으로 입력해주세요.") String email) {
        emailAuthService.sendEmail(email, emailAuthService.createAuthCode(email));
        return Response.success(null);
    }

    @PostMapping("/email-auth-verify")
    public Response<Boolean> verifyEmail(@RequestParam @Email(message = "이메일 형식으로 입력해주세요.") String email,
                                         @RequestParam String code) {
        return Response.success(emailAuthService.verifyEmail(email, code));
    }
}
