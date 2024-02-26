package animores.serverapi.account.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public SignUpResponse signUp(SignUpRequest request) {
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

        return SignUpResponse.toResponse(
                accountRepository.save(Account.toEntity(request, passwordEncoder))
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

    @Override
    @Transactional(readOnly = true)
    public Long signIn(SignInRequest request) throws Exception {
        Account account = accountRepository.findByEmail(request.email())
                .filter(ac -> passwordEncoder.matches(request.password(), ac.getPassword()))// 비밀번호 확인
                .orElseThrow(() -> new Exception());

        return account.getId();
    }

}
