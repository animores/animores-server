package animores.serverapi.to_do.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WeekDay {
    MONDAY("월", 1),
    TUESDAY("화", 2),
    WEDNESDAY("수", 3),
    THURSDAY("목", 4),
    FRIDAY("금", 5),
    SATURDAY("토", 6),
    SUNDAY("일", 7);

    private final String name;
    private final int value;


    private static final Map<String, WeekDay> namesMap =
        Stream.of(WeekDay.values())
            .collect(Collectors.toMap(WeekDay::getName, Function.identity()));

    @JsonCreator
    public static WeekDay fromString(String value) {
        return namesMap.get(value);
    }
}
