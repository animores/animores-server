package animores.serverapi.account.service;

import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;
import animores.serverapi.config.security.RefreshRequest;

public interface AccountService {

    /**
     * accessToken 재발급
     * @param request
     * @return
     */
    SignInResponse refresh(RefreshRequest request) throws Exception;

    /**
     * 계정 생성
     * @param request
     * @return
     */
    SignUpResponse signUp(SignUpRequest request);

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

    /**
     * 로그인
     * @param request
     * @return
     */
    SignInResponse signIn(SignInRequest request) throws Exception;

}
