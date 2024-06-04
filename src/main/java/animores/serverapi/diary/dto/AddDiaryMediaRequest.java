package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일지 미디어 추가 요청")
public record AddDiaryMediaRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId
) {

}
