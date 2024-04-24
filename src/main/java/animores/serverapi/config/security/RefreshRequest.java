package animores.serverapi.config.security;

public record RefreshRequest(

        Long userId,

        String refreshToken

) {
}