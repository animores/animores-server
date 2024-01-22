package animores.serverapi.to_do.dto.request;

import animores.serverapi.to_do.dto.Repeat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ToDoCreateRequest(
        List<Long> petIds,
        Boolean isCustom,
        String title,
        LocalDate date,
        LocalTime time,
        Boolean isAllDay,
        String color,
        Boolean hasAlarm,
        Repeat repeat
) {

}
