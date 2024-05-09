package animores.serverapi.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

@Schema(description = "토큰 재발급 요청")
public record RefreshRequest(
        @NotEmpty(message = "refresh token을 입력 하세요.")
        @Schema(description = "refresh token", example = "refresh token")
        String refreshToken

) {
}