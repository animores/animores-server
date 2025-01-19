package animores.serverapi.to_do.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RepeatUnit {
    HOUR,
    DAY,
    WEEK,
    MONTH;

    private static final Map<String, RepeatUnit> namesMap =
        Stream.of(RepeatUnit.values())
            .collect(Collectors.toMap(RepeatUnit::name, Function.identity()));

    @JsonCreator
    public static RepeatUnit fromString(String value) {
        return namesMap.get(value.toUpperCase());
    }


}
