package animores.serverapi.account.service.impl;


import animores.serverapi.account.domain.auth_mail.AuthMail;
import animores.serverapi.account.domain.auth_mail.ValidMail;
import animores.serverapi.account.repository.auth_mail.AuthMailRepository;
import animores.serverapi.account.repository.auth_mail.ValidMailRepository;
import animores.serverapi.account.service.EmailAuthService;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class GoogleEmailAuthServiceImpl implements EmailAuthService {
    private final Message message;
    private final AuthMailRepository authMailRepository;
    private final ValidMailRepository validMailRepository;

    @Override
    public void sendEmail(String email,String code) {
        try{
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Animores Email Verification");

            StringBuilder stringBuilder = new StringBuilder();
            message.setContent(stringBuilder
                    .append("""
                <h1>Animores Email Verification</h1>
                <p>Here is your verification code:
                """)
                    .append(code)
                    .append("""
                </p>
                """).toString(), "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public String createAuthCode(String email) {
        if (authMailRepository.existsById(email)) {
            authMailRepository.deleteById(email);
        }

        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        authMailRepository.save(new AuthMail(code, email));
        return code;
    }

    @Override
    public boolean verifyEmail(String email, String code) {
        AuthMail authMail = authMailRepository.findById(code).orElseThrow(
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
