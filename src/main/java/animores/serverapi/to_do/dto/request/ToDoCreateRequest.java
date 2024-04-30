package animores.serverapi.to_do.dto.request;

import animores.serverapi.pet.type.Tag;
import animores.serverapi.to_do.dto.RepeatUnit;
import animores.serverapi.to_do.dto.WeekDay;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ToDoCreateRequest(
		@NotNull(message = "펫 아이디는 필수입니다.")
		@NotEmpty(message = "펫 아이디는 비어있을 수 없습니다.")
        List<Long> petIds,
        String content,
		Tag tag,
		@NotNull(message = "날짜는 필수입니다.")
        LocalDate date,
        LocalTime time,
        boolean isAllDay,
        String color,
        boolean isUsingAlarm,
        Repeat repeat
) {
	public ToDoCreateRequest {
		if(tag == null && (content == null || content.isBlank())) {
			throw new IllegalArgumentException("태그와 내용 중 하나는 필수입니다.");
		}

		if(isAllDay && time != null) {
			throw new IllegalArgumentException("하루 종일 일정일 때 시간은 입력할 수 없습니다.");
		}

		if(!isAllDay && time == null) {
			throw new IllegalArgumentException("시간은 필수입니다.");
		}

		if(isAllDay && isUsingAlarm) {
			throw new IllegalArgumentException("하루 종일 일정일 때 알람은 설정할 수 없습니다.");
		}

		if(isAllDay && repeat != null) {
			throw new IllegalArgumentException("하루 종일 일정일 때 반복 설정은 할 수 없습니다.");
		}
	}

	public record Repeat(RepeatUnit unit, Integer interval, List<WeekDay> weekDays){}
}
