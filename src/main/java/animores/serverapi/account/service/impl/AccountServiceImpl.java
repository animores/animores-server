package animores.serverapi.account.service.impl;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.request.SignOutRequest;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.config.security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
    private final BlacklistTokenRepository blacklistTokenRepository;

    @Override
    public SignInResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findById(request.refreshToken())
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN));
        Account account = accountRepository.findById(request.userId())
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        String accessToken = tokenProvider.createToken(String.format("%s:%s", account.getId(), account.getRole()));

        return new SignInResponse(account.getId(), accessToken, LocalDateTime.now().plusHours(tokenProvider.getExpirationHours()), refreshToken.getRefreshToken());
    }

    @Override
    public SignUpResponse signUp(SignUpRequest request) {
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
    public SignInResponse signIn(SignInRequest request) {
        Account account = accountRepository.findByEmail(request.email())
                .filter(ac -> passwordEncoder.matches(request.password(), ac.getPassword()))// 비밀번호 확인
                .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        // at, rt 생성
        String accessToken = tokenProvider.createToken(String.format("%s:%s", account.getId(), account.getRole()));
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken redisRefreshToken = new RefreshToken(refreshToken, account.getId());
        refreshTokenRepository.save(redisRefreshToken);

        return new SignInResponse(account.getId(), accessToken, LocalDateTime.now().plusHours(tokenProvider.getExpirationHours()), refreshToken);
    }

    @Override
    public void signOut(SignOutRequest request, User user) {
        String accessToken = request.accessToken();
        String refreshToken = request.refreshToken();
        Long userId = Long.parseLong(user.getUsername());

        // at 블랙리스트에 넣기
        blacklistTokenRepository.save(new BlackListToken(accessToken, userId));
        // rt 제거
        refreshTokenRepository.deleteById(refreshToken);
    }

}
