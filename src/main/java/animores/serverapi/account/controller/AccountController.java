package animores.serverapi.account.controller;


import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.request.PasswordUpdateRequest;
import animores.serverapi.account.dto.request.SignInRequest;
import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.dto.response.SignInResponse;
import animores.serverapi.account.dto.response.SignUpResponse;
import animores.serverapi.account.entity.Account;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.Response;
import animores.serverapi.common.aop.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Validated
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
    public Response<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return Response.success(accountService.signUp(request));
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    public Response<SignInResponse> signIn(@Valid @RequestBody SignInRequest request) {
        return Response.success(accountService.signIn(request));
    }

    @UserInfo
    @PatchMapping("/update-password")
    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    public Response<Void> updatePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        Account account = accountService.getAccountFromContext();
        accountService.updatePassword(request, account.getId());
        return Response.success(null);
    }

    @UserInfo
    @PatchMapping("/update-nickname")
    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    public Response<Void> updateNickname(@Valid @RequestBody NicknameUpdateRequest request) {
        Account account = accountService.getAccountFromContext();
        accountService.updateNickname(request, account.getId());
        return Response.success(null);
    }

    @GetMapping("/check-nickname/{nickname}")
    @Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크합니다.")
    public Response<Boolean> isDuplicatedNickname(
        @PathVariable @Size(min = 3, max = 20) String nickname) {
        return Response.success(accountService.isDuplicatedNickname(nickname));
    }

    @UserInfo
    @GetMapping("")
    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    public Response<AccountInfoDto> getAccount() {
        Account account = accountService.getAccountFromContext();
        return Response.success(accountService.getAccountInfo(account.getId()));
    }

}
