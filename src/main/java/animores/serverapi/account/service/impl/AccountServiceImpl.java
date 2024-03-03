package animores.serverapi.account.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.config.security.RefreshRequest;
import animores.serverapi.config.security.RefreshToken;
import animores.serverapi.config.security.RefreshTokenRepository;
import animores.serverapi.config.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public SignInResponse refresh(RefreshRequest request) throws Exception {
        RefreshToken refreshToken = refreshTokenRepository.findById(request.refreshToken())
                .orElseThrow(() -> new Exception());
        Account account = accountRepository.findById(request.userId())
                .orElseThrow(() -> new Exception());

        String accessToken = tokenProvider.createToken(String.format("%s:%s", account.getId(), account.getRole()));

        return new SignInResponse(account.getId(), accessToken, LocalDateTime.now().plusHours(tokenProvider.getExpirationHours()), refreshToken.getRefreshToken());
    }

    @Override
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
    public SignInResponse signIn(SignInRequest request) throws Exception {
        Account account = accountRepository.findByEmail(request.email())
                .filter(ac -> passwordEncoder.matches(request.password(), ac.getPassword()))// 비밀번호 확인
                .orElseThrow(() -> new Exception());

        // at, rt 생성
        String accessToken = tokenProvider.createToken(String.format("%s:%s", account.getId(), account.getRole()));
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken redisRefreshToken = new RefreshToken(refreshToken, account.getId());
        refreshTokenRepository.save(redisRefreshToken);

        return new SignInResponse(account.getId(), accessToken, LocalDateTime.now().plusHours(tokenProvider.getExpirationHours()), refreshToken);
    }

}
