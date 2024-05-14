package animores.serverapi.diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record CancelDiaryWishRequest(
    @NotNull
    @NonNull
    @NotEmpty
    Long diaryId,

    @NotNull
    @NonNull
    @NotEmpty
    Long profileId
) {

}
