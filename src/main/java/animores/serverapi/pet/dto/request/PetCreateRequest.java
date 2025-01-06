package animores.serverapi.pet.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "펫 생성 요청")
public record PetCreateRequest(
    @NotNull(message = "종 ID는 필수입니다.")
    @Schema(description = "펫 품종 ID", example = "1")
    Long breedId,
    @Schema(description = "펫 이미지 ID", example = "1")
    Long imageId,
    @NotNull(message = "펫 이름은 필수입니다.")
    @Schema(description = "펫 이름", example = "뽀삐")
    String name,
    @Schema(description = "펫 성별 0은 수컷, 1은 암컷", example = "1", defaultValue = "0")
    Integer gender,
    @Schema(description = "펫 생일", example = "2021-01-01")
    LocalDate birthday,
    @Schema(description = "펫 몸무게", example = "1.25")
    Float weight
) {

}
