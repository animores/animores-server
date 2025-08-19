package animores.serverapi.common.aop;

import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.common.RequestConstants;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * @UserInfo 가 붙은 컨트롤러/메서드 실행 전:
 * - SecurityContext에서 uid 조회
 * - DB에서 Account 조회, 없으면 uid-only로 자동 생성
 * - Account를 Request scope에 저장 (요청 동안 재사용)
 */
@Aspect
@RequiredArgsConstructor
@Component
public class UserInfoAspect {

    private final AccountRepository accountRepository;

    @Pointcut("@annotation(UserInfo) || @within(UserInfo)")
    public void callAt() {}

    @Before("callAt()")
    @Transactional
    public void saveUserInfo() {
        // 1) 인증 정보 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof String)) {
            throw new CustomException(ExceptionCode.INVALID_USER);
        }
        String uid = (String) authentication.getPrincipal();

        // 2) 조회 or 생성 (uid-only 자동 회원가입)
        Account account = accountRepository.findById(uid)
                .orElseGet(() -> accountRepository.save(Account.createWithUid(uid)));

        // 3) Request scope에 저장
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            throw new CustomException(ExceptionCode.INVALID_USER);
        }
        attrs.setAttribute(RequestConstants.ACCOUNT_ATTRIBUTE, account, RequestAttributes.SCOPE_REQUEST);
    }
}
