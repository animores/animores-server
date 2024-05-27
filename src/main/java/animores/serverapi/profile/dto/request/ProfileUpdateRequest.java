package animores.serverapi.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ProfileUpdateRequest(
        @NotNull
        Long profileId,
        @Length(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
        String name
) {
}
