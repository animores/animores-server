package animores.serverapi.security;

public record RefreshRequest(

        Long userId,

        String refreshToken

) {
}