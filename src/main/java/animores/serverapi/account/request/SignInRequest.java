package animores.serverapi.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SignInRequest (

        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @NotEmpty(message = "비밀번호를 입력 하세요.")
        String password,

        @NotNull(message = "자동 로그인 여부를 입력 하세요.")
        Boolean isAutoSignIn

) {
}