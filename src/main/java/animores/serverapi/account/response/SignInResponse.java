package animores.serverapi.account.response;

import java.time.LocalDateTime;

public record SignInResponse(

        Long userId,

        String accessToken,

        LocalDateTime expirationHours,

        String refreshToken

) {
}
