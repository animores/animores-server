package animores.serverapi.account.service;

import animores.serverapi.account.request.AccountCreateRequest;
import animores.serverapi.account.response.AccountCreateResponse;

public interface AccountService {

    /**
     * 계정 생성
     * @param request
     * @return
     */
    AccountCreateResponse createAccount(AccountCreateRequest request);

    /**
     * 이메일 중복 체크
     * @param email
     */
    void emailDuplicationCheck(String email);

    /**
     * 닉네임 중복 체크
     * @param nickname
     */
    void nicknameDuplicationCheck(String nickname);

}
