package animores.serverapi.account.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.dto.request.SignInRequest;
import animores.serverapi.account.dto.request.SignOutRequest;
import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.dto.response.SignInResponse;
import animores.serverapi.account.dto.response.SignUpResponse;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.security.*;
import animores.serverapi.util.RequestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenProvider tokenProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private BlacklistTokenRepository blacklistTokenRepository;
    @InjectMocks
    private AccountServiceImpl accountService;

    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "password";
    private static final Long ACCOUNT_ID = 1L;
    private static final String REFRESH_TOKEN = "refreshToken";

    @Test
    void signUpSuccessfully() {
        SignUpRequest request = new SignUpRequest(EMAIL, PASSWORD, "nickname",false);
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());
        SignUpResponse response = accountService.signUp(request);

        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void signInSuccessfully() {
        SignInRequest request = new SignInRequest(EMAIL, PASSWORD);
        Account account = new TestAccount(ACCOUNT_ID, PASSWORD);

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(tokenProvider.createToken(any(), any())).thenReturn("token");
        when(tokenProvider.getExpirationHours()).thenReturn(1);

        SignInResponse response = accountService.signIn(request);

        assertNotNull(response);
        assertEquals(1L, response.userId());
        verify(accountRepository, times(1)).findByEmail(anyString());
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void signInWithInvalidCredentials() {
        SignInRequest request = new SignInRequest(EMAIL, PASSWORD);
        Account account = new TestAccount(ACCOUNT_ID, "NOT_MATCHED_PASSWORD");

        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(PASSWORD, "NOT_MATCHED_PASSWORD")).thenReturn(false);

        try {
            accountService.signIn(request);
            throw new AssertionError("테스트 실패");
        } catch (Exception e) {
            assertTrue(e instanceof CustomException);
            assertEquals(ExceptionCode.PASSWORD_MISMATCH.name(), ((CustomException) e).getCode().name());
        }
    }


    @Test
    void signInWithPasswordMismatch() {
        SignInRequest request = new SignInRequest(EMAIL, PASSWORD);
        when(accountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        try {
            accountService.signIn(request);
            throw new AssertionError("테스트 실패");
        } catch (Exception e) {
            assertTrue(e instanceof CustomException);
            assertEquals(ExceptionCode.INVALID_USER.name(), ((CustomException) e).getCode().name());
        }
    }

    @Test
    void signOutSuccessfully() {
        SignOutRequest request = new SignOutRequest(REFRESH_TOKEN);
        User user = new User("1", "password", new ArrayList<>());

        given(blacklistTokenRepository.save(any(BlackListToken.class)))
                .willReturn(new BlackListToken("accessToken", 1L));
        doNothing().when(refreshTokenRepository).deleteById(anyString());

        assertDoesNotThrow(() -> accountService.signOut(request, "accessToken", user));
    }

    @Test
    void refreshSuccessfully() {
        RefreshRequest request = new RefreshRequest(REFRESH_TOKEN);

        when(refreshTokenRepository.findById(anyString())).thenReturn(Optional.of(new RefreshToken(REFRESH_TOKEN, ACCOUNT_ID)));
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(new Account()));
        when(tokenProvider.createToken(any(), any())).thenReturn("token");
        when(tokenProvider.getExpirationHours()).thenReturn(1);

        SignInResponse response = accountService.refresh(request, 1L);

        assertNotNull(response);
        verify(refreshTokenRepository, times(1)).findById(anyString());
        verify(accountRepository, times(1)).findById(anyLong());
    }

    @Test
    void refreshWithInvalidToken() {
        RefreshRequest request = new RefreshRequest(REFRESH_TOKEN);
        when(refreshTokenRepository.findById(anyString())).thenReturn(Optional.empty());
        try {
            accountService.refresh(request, 1L);
            throw new AssertionError("테스트 실패");
        } catch (Exception e) {
            assertTrue(e instanceof CustomException);
            assertEquals(ExceptionCode.INVALID_REFRESH_TOKEN.name(), ((CustomException) e).getCode().name());
        }
    }

    @Test
    void isDuplicatedEmail() {
        when(accountRepository.existsByEmail(anyString())).thenReturn(true);

        boolean isDuplicated = accountService.isDuplicatedEmail("test@test.com");

        assertTrue(isDuplicated);
        verify(accountRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void isDuplicatedNickname() {
        when(accountRepository.existsByNickname(anyString())).thenReturn(true);

        boolean isDuplicated = accountService.isDuplicatedNickname("nickname");

        assertTrue(isDuplicated);
        verify(accountRepository, times(1)).existsByNickname(anyString());
    }


    @Test
    void getAccountFromContext() {
        try (MockedStatic<RequestContextHolder> requestContextHolder = mockStatic(RequestContextHolder.class)) {
            // given
            RequestAttributes requestAttributes = mock(RequestAttributes.class);
            requestContextHolder.when(RequestContextHolder::getRequestAttributes)
                    .thenReturn(requestAttributes);

            Account account = new TestAccount(1L);
            given(requestAttributes.getAttribute(RequestConstants.ACCOUNT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST))
                    .willReturn(account);
            // when
            Account result = accountService.getAccountFromContext();
            // then
            assertEquals(1L, result.getId());
        }
    }

        class TestAccount extends Account {
            public TestAccount(Long id) {
                super(id, null, null, null, null, false);
            }

            public TestAccount(Long id, String password) {
                super(id, null, null, password, null, false);
            }
        }
    }
