package animores.serverapi.account.service.impl;

import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.RequestConstants;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicatedNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

    @Override
    public Account getAccountFromContext() {
        try {
            return (Account) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).getAttribute(
                    RequestConstants.ACCOUNT_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        } catch (NullPointerException e) {
            throw new CustomException(ExceptionCode.INVALID_USER);
        }
    }

    @Override
    public AccountInfoDto getAccountInfo(String userId) {
        return AccountInfoDto.fromEntity(
            accountRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER))
        );
    }

    @Override
    public void updateNickname(NicknameUpdateRequest request, String userId) {
        String nickname = request.nickname();

        // 닉네임 중복 검사
        if (isDuplicatedNickname(nickname)) {
            throw new CustomException(ExceptionCode.DUPLICATED_NICKNAME);
        }

        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        account.update(nickname, null);
    }

}
