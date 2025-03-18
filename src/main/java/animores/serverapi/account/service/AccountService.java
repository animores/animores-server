package animores.serverapi.account.service;

import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.request.PasswordUpdateRequest;
import animores.serverapi.account.dto.request.SignInRequest;
import animores.serverapi.account.dto.request.SignUpRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.dto.response.SignInResponse;
import animores.serverapi.account.dto.response.SignUpResponse;
import animores.serverapi.account.entity.Account;

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
     * 닉네임 중복 체크
     *
     * @param nickname
     */
    boolean isDuplicatedNickname(String nickname);

    /**
     * 비밀번호 변경
     *
     * @param request
     */
    void updatePassword(PasswordUpdateRequest request, Long userId);

    /**
     * 닉네임 변경
     *
     * @param request
     */
    void updateNickname(NicknameUpdateRequest request, Long userId);

    Account getAccountFromContext();

    AccountInfoDto getAccountInfo(Long accountId);

}
