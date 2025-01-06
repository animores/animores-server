package animores.serverapi.account.service.impl;

import animores.serverapi.account.entity.auth_mail.AuthMail;
import animores.serverapi.account.entity.auth_mail.ValidMail;
import animores.serverapi.account.repository.auth_mail.AuthMailRepository;
import animores.serverapi.account.repository.auth_mail.ValidMailRepository;
import animores.serverapi.common.exception.CustomException;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoogleEmailAuthServiceImplTest {

    @Mock
    private Message message;

    @Mock
    private AuthMailRepository authMailRepository;

    @Mock
    private ValidMailRepository validMailRepository;

    @InjectMocks
    private GoogleEmailAuthServiceImpl googleEmailAuthService;

    @Test
    void sendEmailSuccessfully() throws MessagingException {
        //given
        ArgumentCaptor<InternetAddress[]> recipients = ArgumentCaptor.forClass(InternetAddress[].class);
        doNothing().when(message).setRecipients(any(Message.RecipientType.class), recipients.capture());

        ArgumentCaptor<String> subject = ArgumentCaptor.forClass(String.class);
        doNothing().when(message).setSubject(subject.capture());

        ArgumentCaptor<String> content = ArgumentCaptor.forClass(String.class);
        doNothing().when(message).setContent(content.capture(), eq("text/html"));

        try (MockedStatic<Transport> utilities = Mockito.mockStatic(Transport.class)) {
            utilities.clearInvocations();

            //when
            googleEmailAuthService.sendEmail("test@test.com", "123456");

            //then
            assertEquals("test@test.com", recipients.getValue()[0].getAddress());
            assertEquals("Animores Email Verification", subject.getValue());
            assertEquals("""
                <h1>Animores Email Verification</h1>
                <p>Here is your verification code:
                123456</p>
                """, content.getValue());
            utilities.verify(() -> Transport.send(any()), times(1));
        }

    }

    @Test
    void createAuthCodeSuccessfully() {
        when(authMailRepository.existsById(anyString())).thenReturn(false);
        when(authMailRepository.save(any(AuthMail.class))).thenReturn(new AuthMail("123456", "test@test.com"));
        String code = googleEmailAuthService.createAuthCode("test@test.com");
        assertNotNull(code);
    }

    @Test
    void verifyEmailSuccessfully() {
        when(authMailRepository.findById(anyString())).thenReturn(Optional.of(new AuthMail("123456", "test@test.com")));
        when(validMailRepository.save(any(ValidMail.class))).thenReturn(new ValidMail("test@test.com"));
        boolean result = googleEmailAuthService.verifyEmail("test@test.com", "123456");
        assertTrue(result);
    }

    @Test
    void verifyEmailNoAuthMail() {
        when(authMailRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(CustomException.class, () -> googleEmailAuthService.verifyEmail("test@test.com", "123456"));
    }

    @Test
    void verifyEmailWithInvalidCode() {
        when(authMailRepository.findById(anyString())).thenReturn(Optional.of(new AuthMail("123456", "test@test.com")));
        boolean result = googleEmailAuthService.verifyEmail("test@test.com", "654321");
        assertFalse(result);
    }
}