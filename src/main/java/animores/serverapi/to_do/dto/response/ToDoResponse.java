package animores.serverapi.to_do.dto.response;

import animores.serverapi.pet.domain.Pet;
import animores.serverapi.to_do.entity.PetToDoRelationship;
import animores.serverapi.to_do.entity.ToDoInstance;

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
				toDoInstance.getToDo().getDate(),
				toDoInstance.getToDo().getTime(),
				toDoInstance.getToDo().isUsingAlarm(),
				toDoInstance.getToDo().getColor(),
				toDoInstance.getCompleteProfile() == null ? null : toDoInstance.getCompleteProfile().getImageUrl(),
				toDoInstance.getCompleteTime()
		);
	}
}
