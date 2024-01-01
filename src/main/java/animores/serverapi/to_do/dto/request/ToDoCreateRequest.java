package animores.serverapi.to_do.dto.request;

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
		Boolean hasAlarm
		// TODO: 반복 관련 부분 추가
) {

}
