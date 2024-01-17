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
        emailDuplicationCheck(request.email());
        nicknameDuplicationCheck(request.nickname());
        if (!request.password().equals(request.confirmPassword())) {
            return null;
        }

        return AccountCreateResponse.toResponse(
                accountRepository.save(Account.toEntity(request))
        );
    }

    @Override
    public void emailDuplicationCheck(String email) {

    }

    @Override
    public void nicknameDuplicationCheck(String nickname) {

    }

}
