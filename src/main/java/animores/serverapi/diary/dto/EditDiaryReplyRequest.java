package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대댓글 수정 요청")
public record EditDiaryReplyRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId,

    @Schema(description = "수정할 대댓글 내용", example = "수정 대댓글입니다.")
    String content
) {

}
