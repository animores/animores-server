package animores.serverapi.account.request;

public record AccountCreateRequest (

        String email,
        String password,
        String confirmPassword,
        String nickname,
        boolean ad_yn

) {
}
