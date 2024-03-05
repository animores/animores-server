package animores.serverapi.account.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AccountCreateRequest (

        @Email(message = "이메일 형식이 아닙니다.")
        String email,

        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,30}$",
                message = "8~30자리 영대소문자, 숫자, 특수문자 조합이어야 합니다.")
        String password,

        @NotEmpty(message = "비밀번호 확인을 입력 하세요.")
        String confirmPassword,

        @NotEmpty(message = "닉네임을 입력 하세요.")
        String nickname,

        @NotNull(message = "광고 수신 여부를 입력 하세요.")
        Boolean isAdPermission

) {
}
