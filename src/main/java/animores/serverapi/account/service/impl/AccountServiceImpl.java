package animores.serverapi.account.service.impl;

import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.request.PasswordUpdateRequest;
import animores.serverapi.account.dto.request.SignInRequest;
import animores.serverapi.account.dto.request.SignOutRequest;
import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.dto.response.SignInResponse;
import animores.serverapi.account.dto.response.SignUpResponse;
import animores.serverapi.account.entity.Account;
import animores.serverapi.account.repository.AccountRepository;
import animores.serverapi.account.service.AccountService;
import animores.serverapi.common.exception.CustomException;
import animores.serverapi.common.exception.ExceptionCode;
import animores.serverapi.security.BlackListToken;
import animores.serverapi.security.BlacklistTokenRepository;
import animores.serverapi.security.RefreshRequest;
import animores.serverapi.security.RefreshToken;
import animores.serverapi.security.RefreshTokenRepository;
import animores.serverapi.security.TokenProvider;
import animores.serverapi.util.RequestConstants;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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
    public SignUpResponse signUp(SignUpRequest request) {
        return SignUpResponse.toResponse(
            accountRepository.save(Account.toEntity(request, passwordEncoder))
        );
    }

    @Override
    public SignInResponse signIn(SignInRequest request) {
        Account account = accountRepository.findByEmail(request.email())
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        if (!passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_MISMATCH);// 비밀번호 확인
        }


        // at, rt 생성
        String accessToken = tokenProvider.createToken(account.getEmail());
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken redisRefreshToken = new RefreshToken(refreshToken, account.getId());
        refreshTokenRepository.save(redisRefreshToken);

        return new SignInResponse(account.getId(), accessToken, LocalDateTime.now().plusHours(tokenProvider.getExpirationHours()), refreshToken);
    }

    @Override
    public void signOut(SignOutRequest request, String accessToken, User user) {
        String refreshToken = request.refreshToken();
        Long userId = Long.parseLong(user.getUsername());

        // at 블랙리스트에 넣기
        blacklistTokenRepository.save(new BlackListToken(accessToken, userId));
        // rt 제거
        refreshTokenRepository.deleteById(refreshToken);
    }

    @Override
    public SignInResponse refresh(RefreshRequest request) {
        RefreshToken refreshToken = refreshTokenRepository.findById(request.refreshToken())
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_REFRESH_TOKEN));
        Account account = accountRepository.findById(refreshToken.getUserId())
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        String accessToken = tokenProvider.createToken(account.getEmail());

        return new SignInResponse(account.getId(), accessToken, LocalDateTime.now().plusHours(tokenProvider.getExpirationHours()), refreshToken.getRefreshToken());
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
    public Account getAccountFromContext() {
        try {
            return (Account) RequestContextHolder.getRequestAttributes().getAttribute(
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
    public void updatePassword(PasswordUpdateRequest request, User user) {
        Long userId = Long.parseLong(user.getUsername());
        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        // 이전 비밀번호와 같은지 검사
        if (passwordEncoder.matches(request.password(), account.getPassword())) {
            throw new CustomException(ExceptionCode.PASSWORD_MATCH_BEFORE);
        }

        String encodedPassword = passwordEncoder.encode(request.password());

        account.update(null, null, encodedPassword, null, null);
    }

    @Override
    public void updateNickname(NicknameUpdateRequest request, User user) {
        String nickname = request.nickname();

        // 닉네임 중복 검사
        if (isDuplicatedNickname(nickname)) {
            throw new CustomException(ExceptionCode.DUPLICATED_NICKNAME);
        }

        Long userId = Long.parseLong(user.getUsername());
        Account account = accountRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ExceptionCode.INVALID_USER));

        account.update(null, null, null, nickname, null);
    }

}
