package animores.serverapi.profile.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

@Schema(description = "프로필 수정 요청")
public record ProfileUpdateRequest(
    @Schema(description = "프로필 ID", example = "1")
    @NotNull
    Long profileId,
    @Schema(description = "프로필 이름", example = "프로필 이름")
    @Length(min = 2, max = 20, message = "이름은 2자 이상 20자 이하로 입력해주세요.")
    String name,
    boolean isUpdateImage
) {

}