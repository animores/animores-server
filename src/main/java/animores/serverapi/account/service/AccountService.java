package animores.serverapi.account.service;

import animores.serverapi.account.dto.request.NicknameUpdateRequest;
import animores.serverapi.account.dto.response.AccountInfoDto;
import animores.serverapi.account.entity.Account;

public interface AccountService {

    /**
     * 닉네임 중복 체크
     *
     * @param nickname
     */
    boolean isDuplicatedNickname(String nickname);

    /**
     * 닉네임 변경
     *
     * @param request
     */
    void updateNickname(NicknameUpdateRequest request, String userId);

    Account getAccountFromContext();

    AccountInfoDto getAccountInfo(String userId);

}
