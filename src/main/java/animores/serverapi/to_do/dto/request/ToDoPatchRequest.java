package animores.serverapi.to_do.dto.request;

import animores.serverapi.to_do.dto.Repeat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ToDoPatchRequest(
		@NotNull
        Long id,
        List<Long> petIds,
        String title,
        LocalDate date,
        LocalTime time,
        boolean isAllDay,
        String color,
        boolean isUsingAlarm,
        Repeat repeat
) {
}
