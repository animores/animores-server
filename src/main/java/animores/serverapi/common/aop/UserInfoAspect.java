package animores.serverapi.common.aop;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.util.RequestConstants;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@RequiredArgsConstructor
@Component
public class UserInfoAspect {

    private final AccountRepository accountRepository;

    private static final Long DEFAULT_ACCOUNT_ID = 13L;
    @Pointcut("@annotation(UserInfo)")
    public void callAt(){

    }

    @Before("callAt()")
    private void saveUserInfo() {
        Account account = accountRepository.findById(DEFAULT_ACCOUNT_ID)
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        RequestContextHolder.getRequestAttributes().setAttribute(
                RequestConstants.ACCOUNT_ATTRIBUTE,
                account,
                RequestAttributes.SCOPE_REQUEST);
    }
}
