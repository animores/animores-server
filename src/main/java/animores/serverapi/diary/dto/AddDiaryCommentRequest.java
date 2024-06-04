package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "댓글 작성 요청")
public record AddDiaryCommentRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId,

    @Schema(description = "일지 아이디", example = "1")
    Long diaryId,

    @Schema(description = "댓글 내용", example = "댓글입니다.")
    String content
) {

}
