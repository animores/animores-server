package animores.serverapi.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "로그인 요청")
public record SignInRequest (

        @Email(message = "이메일 형식이 아닙니다.")
        @Schema(description = "이메일", example = "abc@gmail.com")
        String email,

        @NotEmpty(message = "비밀번호를 입력 하세요.")
        @Schema(description = "비밀번호", example = "abc123!@#")
        String password

) {
}