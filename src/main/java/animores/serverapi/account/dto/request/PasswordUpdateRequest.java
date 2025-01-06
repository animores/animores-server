package animores.serverapi.account.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateRequest(

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,30}$",
        message = "8~30자리 영대소문자, 숫자, 특수문자 조합이어야 합니다.")
    String password,

    @NotEmpty(message = "비밀번호 확인을 입력 하세요.")
    String confirmPassword

) {

}
