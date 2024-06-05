package animores.serverapi.account.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "회원가입 요청")
public record SignUpRequest(

        @Email(message = "이메일 형식이 아닙니다.")
        @Schema(description = "이메일", example = "abc@gmail.com")
        String email,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,30}$",
                message = "8~30자리 영대소문자, 숫자, 특수문자 조합이어야 합니다.")
        @Schema(description = "비밀번호", example = "abc123!@#")
        String password,

        @NotEmpty(message = "닉네임을 입력 하세요.")
        @Schema(description = "닉네임", example = "닉네임")
        String nickname,

        @NotNull(message = "광고 수신 여부를 입력 하세요.")
        @Schema(description = "광고 수신 여부", example = "true")
        Boolean isAdPermission

) {
}
