package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대댓글 작성 요청")
public record AddDiaryReplyRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId,

    @Schema(description = "댓글 아이디", example = "1")
    Long diaryCommentId,

    @Schema(description = "대댓글 내용", example = "대댓글입니다.")
    String content
) {

}
