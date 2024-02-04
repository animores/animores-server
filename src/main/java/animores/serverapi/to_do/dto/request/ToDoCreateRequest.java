package animores.serverapi.to_do.dto.request;

import animores.serverapi.pet.type.Tag;
import animores.serverapi.to_do.dto.Repeat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ToDoCreateRequest(
        List<Long> petIds,
        boolean isCustom,
        String title,
		Tag tag,
		@NotNull
        LocalDate date,
        LocalTime time,
        boolean isAllDay,
        String color,
        boolean isUsingAlarm,
        Repeat repeat
) {

}
