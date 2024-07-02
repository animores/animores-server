package animores.serverapi.account.dto.response;

import java.time.LocalDateTime;

public record SignInResponse(

        Long userId,

        String accessToken,

        LocalDateTime expirationHours,

        String refreshToken

) {
}
