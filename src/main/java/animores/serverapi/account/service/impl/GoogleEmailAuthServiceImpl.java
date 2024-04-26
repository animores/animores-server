package animores.serverapi.account.service.impl;


import animores.serverapi.account.domain.auth_mail.AuthMail;
import animores.serverapi.account.domain.auth_mail.ValidMail;
import animores.serverapi.account.repository.auth_mail.AuthMailRepository;
import animores.serverapi.account.repository.auth_mail.ValidMailRepository;
import animores.serverapi.account.service.EmailAuthService;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GoogleEmailAuthServiceImpl implements EmailAuthService {
    private final JavaMailSender emailSender;
    private final AuthMailRepository authMailRepository;
    private final ValidMailRepository validMailRepository;

    @Override
    public void sendEmail(String email,String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@animores.com");
        message.setTo(email);
        message.setSubject("Animores Email Verification");
        StringBuilder stringBuilder = new StringBuilder();
        message.setText(stringBuilder
                .append("""
                <h1>Animores Email Verification</h1>
                <p>Here is your verification code:
                """)
                .append(code)
                .append("""
                </p>
                """).toString());
        emailSender.send(message);
    }

    @Override
    @Transactional
    public String createAuthCode(String email) {
        if (authMailRepository.existsById(email)) {
            authMailRepository.deleteById(email);
        }

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        authMailRepository.save(new AuthMail(email, code));
        return code;
    }

    @Override
    public boolean verifyEmail(String email, String code) {
        AuthMail authMail = authMailRepository.findById(email).orElseThrow(
                () -> new CustomException(ExceptionCode.EXPIRED_AUTH_CODE)
        );

        String codeToMatch = authMail.getCode();
        authMailRepository.deleteById(email);

        if (codeToMatch.equals(code)) {
            validMailRepository.save(new ValidMail(email));
            return true;
        }

        return false;
    }
}
