package animores.serverapi.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "일지 미디어 수정 요청")
public record EditDiaryMediaRequest(
    @Schema(description = "프로필 아이디", example = "1")
    Long profileId,

    @Schema(description = "삭제할 미디어 id 목록입니다.", example = "[1, 2, 3]")
    List<Long> mediaIds
) {

}
