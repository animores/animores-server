package animores.serverapi.account.response;

import animores.serverapi.account.domain.Account;

public record AccountCreateResponse(

        Long id,

        String email,

        String password,

        String nickname,

        boolean ad_yn

) {

        public static AccountCreateResponse toResponse(Account entity) {
                return new AccountCreateResponse(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getNickname(), entity.isAd_yn());
        }

}
