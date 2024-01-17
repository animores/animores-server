package animores.serverapi.account.service.impl;

import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public void createAccount(AccountCreateRequest request) {

    }

}
