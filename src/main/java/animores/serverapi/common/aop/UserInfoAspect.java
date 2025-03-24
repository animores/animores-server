package animores.serverapi.common.aop;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.common.RequestConstants;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@RequiredArgsConstructor
@Component
public class UserInfoAspect {

    private final AccountRepository accountRepository;

    @Pointcut("@annotation(UserInfo) || @within(UserInfo)")  // 특정 애노테이션이 있는 API만 실행됨
    public void callAt() {}

    @Before("callAt()")  // API가 실행되기 전에 실행됨
    private void saveUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 헤더에서 userId 가져오기
        String userIdStr = request.getHeader("userId");
        if (userIdStr == null || userIdStr.isEmpty()) {
            throw new CustomException(ExceptionCode.INVALID_USER);
        }

        // userId로 Account 조회
        Long userId = Long.parseLong(userIdStr);
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        // RequestContextHolder에 저장 (API에서 바로 사용 가능)
        RequestContextHolder.getRequestAttributes().setAttribute(
                RequestConstants.ACCOUNT_ATTRIBUTE, account, RequestAttributes.SCOPE_REQUEST);
    }
}