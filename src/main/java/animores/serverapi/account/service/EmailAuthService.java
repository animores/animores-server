package animores.serverapi.account.service;

public interface EmailAuthService {
    void sendEmail(String email, String title, String message);
    boolean verifyEmail(String email, String code);

    String createAuthCode(String email);
}
