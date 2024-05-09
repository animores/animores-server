package animores.serverapi.account.service;

import animores.serverapi.account.domain.Account;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.request.SignOutRequest;
import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.security.RefreshRequest;
import org.springframework.security.core.userdetails.User;

public interface AccountService {
    /**
     * 계정 생성
     * @param request
     * @return
     */
    SignUpResponse signUp(SignUpRequest request);

    /**
     * 로그인
     * @param request
     * @return
     */
    SignInResponse signIn(SignInRequest request);

    /**
     * 로그아웃
     * @param request
     */
    void signOut(SignOutRequest request, String token, User user);

    /**
     * accessToken 재발급
     * @param request
     * @return
     */
    SignInResponse refresh(RefreshRequest request, Long userId);

    /**
     * 이메일 중복 체크
     * @param email
     */
    boolean isDuplicatedEmail(String email);

    /**
     * 닉네임 중복 체크
     * @param nickname
     */
    boolean isDuplicatedNickname(String nickname);

    Account getAccountFromContext();

}
