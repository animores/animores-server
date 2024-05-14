package animores.serverapi.diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

public record AddDiaryWishRequest(

    @NotNull
    @NonNull
    @NotEmpty
    Long diaryId
) {

}
