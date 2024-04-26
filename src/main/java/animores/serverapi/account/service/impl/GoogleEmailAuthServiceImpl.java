package animores.serverapi.account.service.impl;


import animores.serverapi.account.repository.auth_mail.AuthMailRepository;
import animores.serverapi.account.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GoogleEmailAuthServiceImpl implements EmailAuthService {

    private final JavaMailSender emailSender;
    private final AuthMailRepository authMailRepository;

    @Override
    public void sendEmail(String email, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@animores.com");
        message.setTo(email);
        message.setSubject(title);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public String createAuthCode(String email) {
        return null;
    }

    @Override
    public boolean verifyEmail(String email, String code) {
        return false;
    }
}
