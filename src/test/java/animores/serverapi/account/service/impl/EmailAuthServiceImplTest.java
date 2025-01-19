package animores.serverapi.account.service.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import animores.serverapi.account.entity.auth_mail.AuthMail;
import animores.serverapi.account.entity.auth_mail.ValidMail;
import animores.serverapi.account.repository.auth_mail.AuthMailRepository;
import animores.serverapi.account.repository.auth_mail.ValidMailRepository;
import animores.serverapi.common.exception.CustomException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.messaging.Message;

@ExtendWith(MockitoExtension.class)
class EmailAuthServiceImplTest {

    private QueueMessagingTemplate sqsTemplate = Mockito.mock(QueueMessagingTemplate.class);
    private AuthMailRepository authMailRepository = Mockito.mock(AuthMailRepository.class);
    private ValidMailRepository validMailRepository = Mockito.mock(ValidMailRepository.class);
    private EmailAuthServiceImpl googleEmailAuthService =
        new EmailAuthServiceImpl(sqsTemplate, "EmailQueue", authMailRepository,
            validMailRepository);

    @Test
    void sendEmailSuccessfully() {
        //when
        googleEmailAuthService.sendEmail("test@test.com", "123456");

        //then
        verify(sqsTemplate, times(1)).convertAndSend(anyString(), any(Message.class));
    }

    @Test
    void createAuthCodeSuccessfully() {
        when(authMailRepository.existsById(anyString())).thenReturn(false);
        when(authMailRepository.save(any(AuthMail.class))).thenReturn(
            new AuthMail("123456", "test@test.com"));
        String code = googleEmailAuthService.createAuthCode("test@test.com");
        assertNotNull(code);
    }

    @Test
    void verifyEmailSuccessfully() {
        when(authMailRepository.findById(anyString())).thenReturn(
            Optional.of(new AuthMail("123456", "test@test.com")));
        when(validMailRepository.save(any(ValidMail.class))).thenReturn(
            new ValidMail("test@test.com"));
        boolean result = googleEmailAuthService.verifyEmail("test@test.com", "123456");
        assertTrue(result);
    }

    @Test
    void verifyEmailNoAuthMail() {
        when(authMailRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomException.class,
            () -> googleEmailAuthService.verifyEmail("test@test.com", "123456"));
    }

    @Test
    void verifyEmailWithInvalidCode() {
        when(authMailRepository.findById(anyString())).thenReturn(
            Optional.of(new AuthMail("123456", "test@test.com")));
        boolean result = googleEmailAuthService.verifyEmail("test@test.com", "654321");
        assertFalse(result);
    }
}
