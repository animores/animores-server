package animores.serverapi.account.service;

public interface EmailAuthService {
    void sendEmail(String email, String code);
    boolean verifyEmail(String email, String code);

    String createAuthCode(String email);
}
