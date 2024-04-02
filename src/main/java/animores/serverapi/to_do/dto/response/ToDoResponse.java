package animores.serverapi.to_do.dto.response;

import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record ToDoResponse(
		Long id,
		String title,
		List<PetResponse> pets,
		boolean isAllDay,
		LocalDate date,
		LocalTime time,
		boolean isUsingAlarm,
		String color,
		String completeProfileImage,
		LocalDateTime completeDateTime

) {

	public static ToDoResponse fromToDoInstanceVo(ToDoInstanceVo toDoInstanceVo, List<PetResponse> pets) {

		return new ToDoResponse(
				toDoInstanceVo.toDo().id(),
				toDoInstanceVo.toDo().tag() == null ?  toDoInstanceVo.toDo().content() : toDoInstanceVo.toDo().tag().name(),
				pets,
				toDoInstanceVo.toDo().isAllDay(),
				toDoInstanceVo.date(),
				toDoInstanceVo.time(),
				toDoInstanceVo.toDo().isUsingAlarm(),
				toDoInstanceVo.toDo().color(),
				toDoInstanceVo.completeProfile() == null ? null : toDoInstanceVo.completeProfile().imageUrl(),
				toDoInstanceVo.completeTime()
		);
	}
}
