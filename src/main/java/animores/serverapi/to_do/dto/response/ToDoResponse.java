package animores.serverapi.to_do.dto.response;

import animores.serverapi.pet.domain.Pet;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDoInstance;
import animores.serverapi.to_do.entity.vo.ToDoInstanceVo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

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

	public static ToDoResponse fromToDoInstance(ToDoInstance toDoInstance, Map<Long, String> petNameMap) {

		List<Long> petIds = toDoInstance.getToDo().getPetToDoRelationships().stream()
				.map(PetToDoRelationship::getPet)
				.map(Pet::getId)
				.toList();

		return new ToDoResponse(
				toDoInstance.getId(),
				toDoInstance.getToDo().getContent(),
				petIds.stream().map(id -> new PetResponse(id, petNameMap.get(id))).toList(),
				toDoInstance.getToDo().isAllDay(),
				toDoInstance.getDate(),
				toDoInstance.getTime(),
				toDoInstance.getToDo().isUsingAlarm(),
				toDoInstance.getToDo().getColor(),
				toDoInstance.getCompleteProfile() == null ? null : toDoInstance.getCompleteProfile().getImageUrl(),
				toDoInstance.getCompleteTime()
		);
	}

	public static ToDoResponse fromToDoInstanceVo(ToDoInstanceVo toDoInstanceVO, Map<Long, String> petNameMap) {

		List<Long> petIds = toDoInstanceVO.toDo().petToDoRelationships().stream()
				.map(PetToDoRelationship::getPet)
				.map(Pet::getId)
				.toList();

		return new ToDoResponse(
				toDoInstanceVO.id(),
				toDoInstanceVO.toDo().tag() == null ?  toDoInstanceVO.toDo().content() : toDoInstanceVO.toDo().tag().name(),
				petIds.stream().map(id -> new PetResponse(id, petNameMap.get(id))).toList(),
				toDoInstanceVO.toDo().isAllDay(),
				toDoInstanceVO.date(),
				toDoInstanceVO.time(),
				toDoInstanceVO.toDo().isUsingAlarm(),
				toDoInstanceVO.toDo().color(),
				toDoInstanceVO.completeProfile() == null ? null : toDoInstanceVO.completeProfile().imageUrl(),
				toDoInstanceVO.completeTime()
		);
	}
}
