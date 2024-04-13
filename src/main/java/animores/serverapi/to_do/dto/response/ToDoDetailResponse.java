package animores.serverapi.to_do.dto.response;

import animores.serverapi.to_do.dto.RepeatUnit;
import animores.serverapi.to_do.dto.WeekDay;
import animores.serverapi.to_do.entity.ToDo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ToDoDetailResponse(
        Long id,
        String title,
        List<PetResponse> pets,
        boolean isAllDay,
        LocalDate date,
        LocalTime time,
        boolean isUsingAlarm,
        RepeatUnit unit,
        Integer intervalNum,
        List<WeekDay> weekDays,
        String color
) {

    public static ToDoDetailResponse fromToDo(ToDo toDo) {
        return new ToDoDetailResponse(toDo.getId(),
                toDo.getTag() == null ? toDo.getContent() : toDo.getTag().name(),
                toDo.getPetToDoRelationships().stream().map(PetResponse::fromPetToDoRelationship).toList(),
                toDo.isAllDay(),
                toDo.getDate(),
                toDo.getTime(),
                toDo.isUsingAlarm(),
                toDo.getUnit(),
                toDo.getIntervalNum(),
                toDo.getWeekDays(),
                toDo.getColor());
    }
}
