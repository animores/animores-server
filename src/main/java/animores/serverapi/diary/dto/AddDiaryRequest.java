package animores.serverapi.diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddDiaryRequest(
    @NotNull(message = "내용을 입력해주세요.")
    @NotEmpty(message = "내용을 입력해주세요.")
    String content
) {

}
