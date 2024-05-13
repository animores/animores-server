package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일지 내용 수정 요청")
public record EditDiaryContentRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId,

    @Schema(description = "내용", example = "일지 수정 내용입니다.")
    String content
) {

}
