package animores.serverapi.account.service.impl;

import animores.serverapi.account.dto.request.EmailQueueBody;
import animores.serverapi.account.entity.auth_mail.AuthMail;
import animores.serverapi.account.entity.auth_mail.ValidMail;
import animores.serverapi.account.repository.auth_mail.AuthMailRepository;
import animores.serverapi.account.repository.auth_mail.ValidMailRepository;
import animores.serverapi.account.service.EmailAuthService;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

@Service
public class EmailAuthServiceImpl implements EmailAuthService {

    private final QueueMessagingTemplate sqsTemplate;
    private final String mailQueueName;
    private final AuthMailRepository authMailRepository;
    private final ValidMailRepository validMailRepository;
    private static final Map<String, Object> HEADERS = new HashMap<>();

    static {
        HEADERS.put("MessageGroupId", "email");
        HEADERS.put("contentType", MimeTypeUtils.APPLICATION_JSON);
    }

    public EmailAuthServiceImpl(QueueMessagingTemplate sqsTemplate,
        @Value("${spring.cloud.aws.sqs.mail-queue}") String mailQueueName,
        AuthMailRepository authMailRepository, ValidMailRepository validMailRepository) {
        this.sqsTemplate = sqsTemplate;
        this.mailQueueName = mailQueueName;
        this.authMailRepository = authMailRepository;
        this.validMailRepository = validMailRepository;
    }

    @Override
    public void sendEmail(String email, String code) {
        sqsTemplate.convertAndSend(mailQueueName, MessageBuilder.createMessage(
            new EmailQueueBody(email, code),
            new MessageHeaders(HEADERS)
        ));
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

        if (codeToMatch.equals(code)) {
            authMailRepository.deleteById(email);
            validMailRepository.save(new ValidMail(email));
            return true;
        }

        return false;
    }

    @Override
    public void checkVerifiedEmail(String email) {
        if (!validMailRepository.existsById(email)) {
            throw new CustomException(ExceptionCode.NOT_VERIFIED_EMAIL);
        }
    }
}
