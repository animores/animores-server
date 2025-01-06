package animores.serverapi.account.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NicknameUpdateRequest(

    @NotBlank
    @Size(min = 3, max = 20, message = "3 ~ 20자리 닉네임이 아닙니다.")
    String nickname

) {

}
