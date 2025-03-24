package animores.serverapi.account.service.impl;

import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.request.PasswordUpdateRequest;
import animores.serverapi.account.dto.request.SignInRequest;
import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.dto.response.SignInResponse;
import animores.serverapi.account.dto.response.SignUpResponse;
import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.util.RequestConstants;
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
    public SignUpResponse signUp(SignUpRequest request) {
        return SignUpResponse.toResponse(
            accountRepository.save(Account.toEntity(request))
        );
    }

    @Override
    public SignInResponse signIn(SignInRequest request) {
        Account account = accountRepository.findByEmail(request.email())
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        if (!request.password().equals(account.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_MISMATCH);// 비밀번호 확인
        }

        return new SignInResponse(account.getId());
    }

    // TODO: 로그아웃 나중에 구현

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
    public AccountInfoDto getAccountInfo(Long accountId) {
        return AccountInfoDto.fromEntity(
            accountRepository.findById(accountId)
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER))
        );
    }

    @Override
    public void updatePassword(PasswordUpdateRequest request, Long userId) {
        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        // 이전 비밀번호와 같은지 검사 (임시이므로 인코딩 x)
        if (!request.password().equals(account.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_MATCH_BEFORE);
        }

        account.update(null, null, request.password(), null, null);
    }

    @Override
    public void updateNickname(NicknameUpdateRequest request, Long userId) {
        String nickname = request.nickname();

        // 닉네임 중복 검사
        if (isDuplicatedNickname(nickname)) {
            throw new CustomException(ExceptionCode.DUPLICATED_NICKNAME);
        }

        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        account.update(null, null, null, nickname, null);
    }

}
