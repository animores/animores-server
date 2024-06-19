package animores.serverapi.account.controller;


import animores.serverapi.account.domain.Account;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.request.SignOutRequest;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.account.service.EmailAuthService;
import animores.serverapi.common.Response;
import animores.serverapi.common.aop.UserInfo;
import animores.serverapi.security.RefreshRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    public Response<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        emailAuthService.checkVerifiedEmail(request.email());
        return Response.success(accountService.signUp(request));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    public Response<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return Response.success(accountService.signIn(request));
    }

    @PostMapping("/sign-out")
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
    public ResponseEntity<Void> signOut(@Valid @RequestBody SignOutRequest request,
                                        @RequestHeader("Authorization") String token,
                                        @AuthenticationPrincipal User user) {
        accountService.signOut(request, token, user);
        return ResponseEntity.noContent().build();
    }

    @UserInfo
    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('USER')")
    @SecurityRequirement(name = "Authorization")
    @Operation(summary = "토큰 재발급", description = "Access 토큰을 재발급합니다.")
    public Response<SignInResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        Account account = accountService.getAccountFromContext();
        return Response.success(accountService.refresh(request, account.getId()));
    }

    @GetMapping("/check-email/{email}")
    @Operation(summary = "이메일 중복 체크", description = "이메일 중복을 체크합니다.")
    public Response<Boolean> isDuplicatedEmail(@PathVariable @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$") String email) {
        return Response.success(accountService.isDuplicatedEmail(email));
    }

    @GetMapping("/check-nickname/{nickname}")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크합니다.")
    public Response<Boolean> isDuplicatedNickname(@PathVariable @Size(min = 3, max = 20) String nickname) {
        return Response.success(accountService.isDuplicatedNickname(nickname));
    }

    @PostMapping("/email-auth-create")
    @Operation(summary = "이메일 인증코드 생성", description = "이메일 인증코드를 생성합니다.")
    public Response<Void> sendAuthEmail(@RequestParam @Email(message = "이메일 형식으로 입력해주세요.") String email) {
        emailAuthService.sendEmail(email, emailAuthService.createAuthCode(email));
        return Response.success(null);
    }

    @PostMapping("/email-auth-verify")
    @Operation(summary = "이메일 인증코드 확인", description = "이메일 인증코드를 확인합니다.")
    public Response<Boolean> verifyEmail(@RequestParam @Email(message = "이메일 형식으로 입력해주세요.") String email,
                                         @RequestParam String code) {
        return Response.success(emailAuthService.verifyEmail(email, code));
    }
}
