package animores.serverapi.account.service;

import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.request.PasswordUpdateRequest;
import animores.serverapi.account.dto.request.SignInRequest;
import animores.serverapi.account.dto.request.SignOutRequest;
import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.dto.response.SignInResponse;
import animores.serverapi.account.dto.response.SignUpResponse;
import animores.serverapi.account.entity.Account;
import animores.serverapi.security.RefreshRequest;
import org.springframework.security.core.userdetails.User;

public interface AccountService {

    /**
     * 계정 생성
     *
     * @param request
     * @return
     */
    SignUpResponse signUp(SignUpRequest request);

    /**
     * 로그인
     *
     * @param request
     * @return
     */
    SignInResponse signIn(SignInRequest request);

    /**
     * 로그아웃
     *
     * @param request
     */
    void signOut(SignOutRequest request, String token, User user);

    /**
     * accessToken 재발급
     *
     * @param request
     * @return
     */
    SignInResponse refresh(RefreshRequest request);

    /**
     * 이메일 중복 체크
     *
     * @param email
     */
    boolean isDuplicatedEmail(String email);

    /**
     * 닉네임 중복 체크
     *
     * @param nickname
     */
    boolean isDuplicatedNickname(String nickname);

    /**
     * 비밀번호 변경
     *
     * @param request
     * @param user
     */
    void updatePassword(PasswordUpdateRequest request, User user);

    /**
     * 닉네임 변경
     *
     * @param request
     * @param user
     */
    void updateNickname(NicknameUpdateRequest request, User user);

    Account getAccountFromContext();

    AccountInfoDto getAccountInfo(Long accountId);

}
