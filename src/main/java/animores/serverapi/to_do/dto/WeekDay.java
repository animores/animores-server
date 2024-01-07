package animores.serverapi.to_do.dto;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum WeekDay {
	MONDAY,
	TUESDAY,
	WEDNESDAY,
	THURSDAY,
	FRIDAY,
	SATURDAY,
	SUNDAY;

	private static final Map<String, WeekDay> namesMap =
			Stream.of(WeekDay.values())
			.collect(Collectors.toMap(WeekDay::name, Function.identity()));

	public static WeekDay fromString(String value) {
		return namesMap.get(value.toUpperCase());
	}
}
