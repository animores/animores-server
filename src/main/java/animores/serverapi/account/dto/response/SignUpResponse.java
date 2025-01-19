package animores.serverapi.account.dto.response;

import animores.serverapi.account.entity.Account;

public record SignUpResponse(

    Long id,

    String email,

    String nickname,

    Boolean isAdPermission

) {

    public static SignUpResponse toResponse(Account entity) {
        return new SignUpResponse(entity.getId(), entity.getEmail(), entity.getPassword(),
            entity.getIsAdPermission());
    }

}
