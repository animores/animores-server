package animores.serverapi.account.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "로그아웃 요청")
public record SignOutRequest(
        @Schema(description = "refresh token", example = "refresh token")
        @NotEmpty(message = "refresh token을 입력 하세요.")
        String refreshToken

) {
}