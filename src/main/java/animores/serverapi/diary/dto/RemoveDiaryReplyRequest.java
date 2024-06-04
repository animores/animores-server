package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "대댓글 삭제 요청")
public record RemoveDiaryReplyRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId
) {

}
