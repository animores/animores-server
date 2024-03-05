package animores.serverapi.account.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.response.AccountCreateResponse;
import animores.serverapi.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountCreateResponse createAccount(AccountCreateRequest request) {
        // 검증
        if (!request.isAdPermission()) {
            return null;
        }
        if (isDuplicatedEmail(request.email())) {
            return null;
        }
        if (isDuplicatedNickname(request.nickname())) {
            return null;
        }
        if (!request.password().equals(request.confirmPassword())) {
            return null;
        }

        // TODO: 패스워드 암호화 테스트할 때 귀찮으니 나~중에 해도댐

        return AccountCreateResponse.toResponse(
                accountRepository.save(Account.toEntity(request))
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicatedEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicatedNickname(String nickname) {
        return accountRepository.existsByNickname(nickname);
    }

}
