package animores.serverapi.account.service;

import animores.serverapi.account.request.SignUpRequest;
import animores.serverapi.account.request.SignInRequest;
import animores.serverapi.account.response.SignInResponse;
import animores.serverapi.account.response.SignUpResponse;

public interface AccountService {

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
     * sign-in
     * @param request
     * @return
     */
    SignInResponse signIn(SignInRequest request) throws Exception;
}
