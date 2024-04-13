package animores.serverapi.to_do.dto;

import jakarta.persistence.Embeddable;

import java.util.List;

@Embeddable
public record Repeat(
        RepeatUnit unit,
        Integer interval,
        List<WeekDay> weekDays
) {
}
