package animores.serverapi.diary.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EditDiaryMediaRequest(
    @NotNull
    @NotEmpty
    List<Long> mediaIds
) {

}
