package animores.serverapi.to_do.dto;

import jakarta.persistence.Embeddable;
import java.util.Collections;
import java.util.List;

@Embeddable
public record Repeat(
    RepeatUnit unit,
    Integer interval,
    List<WeekDay> weekDays
) {

    public Repeat() {
        this(null, 0, Collections.EMPTY_LIST);
    }
}

