package animores.serverapi.account.aop;

import animores.serverapi.account.domain.Account;
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

    @Pointcut("@annotation(UserInfo)")
    public void callAt(){

    }

    @Before("callAt()")
    private void saveUserInfo() {
        Account account = accountRepository.findById(Long.parseLong(RequestContextHolder.getRequestAttributes().getAttribute(
                        RequestConstants.ACCOUNT_ID_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST).toString()
                ))
                .orElseThrow(() -> new RuntimeException("Invalid user"));

        RequestContextHolder.getRequestAttributes().setAttribute(
                RequestConstants.ACCOUNT_ATTRIBUTE,
                account,
                RequestAttributes.SCOPE_REQUEST);
    }
}
