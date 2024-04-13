package animores.serverapi.to_do.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum WeekDay {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    private int value;

    private static final Map<String, WeekDay> namesMap =
            Stream.of(WeekDay.values())
                    .collect(Collectors.toMap(WeekDay::name, Function.identity()));

    public static WeekDay fromString(String value) {
        return namesMap.get(value.toUpperCase());
    }
}
