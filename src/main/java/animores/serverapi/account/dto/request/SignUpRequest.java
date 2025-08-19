package animores.serverapi.account.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Schema(description = "회원가입 요청")
public record SignUpRequest(

    @NotEmpty(message = "닉네임을 입력 하세요.")
    @Schema(description = "닉네임", example = "닉네임")
    String nickname,

    @NotNull(message = "광고 수신 여부를 입력 하세요.")
    @Schema(description = "광고 수신 여부", example = "true")
    Boolean isAdPermission

) {

}
