package animores.serverapi.common.aop;

import static animores.serverapi.common.RequestConstants.ACCOUNT_ATTRIBUTE;
import static animores.serverapi.common.RequestConstants.ACCOUNT_ID;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.security.TokenProvider;
import animores.serverapi.util.RequestConstants;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.nio.file.AccessDeniedException;

@Aspect
@RequiredArgsConstructor
@Component
public class UserInfoAspect {

    private final AccountRepository accountRepository;
    private final TokenProvider tokenProvider;

    @Pointcut("@annotation(UserInfo) || @within(UserInfo)")
    public void callAt() {

    }

    @Before("callAt()")
    private void saveUserInfo() throws AccessDeniedException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (token == null) {
            token = request.getParameter("token");
        }

        if (token == null) {
            throw new AccessDeniedException("Empty token");
        }

        try {
            String email = tokenProvider.getEmailFromToken(token);
            Account account = accountRepository.findByEmail(email).orElseThrow(() -> new AccessDeniedException("Invalid token"));
            if (account == null || !account.getEmail().equals(email)) {
                throw new AccessDeniedException("Invalid token");
            }

            RequestContextHolder.getRequestAttributes().setAttribute(ACCOUNT_ATTRIBUTE, account, RequestAttributes.SCOPE_REQUEST);
        } catch (Exception e) {
            throw new AccessDeniedException("Token expired");
        }
    }
}