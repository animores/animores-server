package animores.serverapi.account.response;

import animores.serverapi.account.domain.Account;

public record SignUpResponse(

        Long id,

        String email,

        String password,

        String nickname,

        boolean isAdPermission

) {

        public static SignUpResponse toResponse(Account entity) {
                return new SignUpResponse(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getNickname(), entity.isAdPermission());
        }

}
