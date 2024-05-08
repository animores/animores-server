package animores.serverapi.account.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.security.BlacklistTokenRepository;
import animores.serverapi.security.RefreshTokenRepository;
import animores.serverapi.security.TokenProvider;
import animores.serverapi.util.RequestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

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
        }
    }
