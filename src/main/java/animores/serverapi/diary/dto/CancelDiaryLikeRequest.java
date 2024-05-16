package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "일지 좋아요 취소 요청")
public record CancelDiaryLikeRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId
) {

}
