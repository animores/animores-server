package animores.serverapi.account.request;

import jakarta.validation.constraints.NotEmpty;

public record SignOutRequest(
        @NotEmpty(message = "refreshToken을 입력 하세요.")
        String refreshToken

) {
}