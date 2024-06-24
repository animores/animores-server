package animores.serverapi.account.dto.response;

import animores.serverapi.account.entity.Account;

public record AccountCreateResponse(

        Long id,

        String email,

        String password,

        String nickname,

        boolean isAdPermission

) {

        public static AccountCreateResponse toResponse(Account entity) {
                return new AccountCreateResponse(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getNickname(), entity.isAdPermission());
        }

}
