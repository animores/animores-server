package animores.serverapi.diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record AddDiaryRequest(
    @NotNull
    @NotEmpty
    Long accountId,
    @NotNull
    @NotEmpty
    Long profileId,
    @NotNull
    @NotEmpty
    String content
) {

}
