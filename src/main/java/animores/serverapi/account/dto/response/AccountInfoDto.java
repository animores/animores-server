package animores.serverapi.account.dto.response;

import animores.serverapi.account.entity.Account;

public record AccountInfoDto(
    String id,
    String nickname,
    Boolean isAdPermission
) {

    public static AccountInfoDto fromEntity(Account account) {
        return new AccountInfoDto(account.getId(),
            account.getNickname(),
            account.getIsAdPermission());
    }
}
