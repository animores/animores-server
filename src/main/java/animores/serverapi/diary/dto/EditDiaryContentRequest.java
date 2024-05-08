package animores.serverapi.diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EditDiaryContentRequest(
    @NotNull
    @NotEmpty
    String content
) {

}
