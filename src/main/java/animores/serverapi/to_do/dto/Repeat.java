package animores.serverapi.to_do.dto;

import java.util.List;

public record Repeat(
        RepeatUnit unit,
        Integer interval,
        List<WeekDay> weekDays
) {
}
