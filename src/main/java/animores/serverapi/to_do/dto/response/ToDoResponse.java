package animores.serverapi.to_do.dto.response;

import animores.serverapi.to_do.dao.ToDoInstanceDao;
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

    public static ToDoResponse fromToDoInstanceDao(ToDoInstanceDao toDoInstanceDao,
        List<PetResponse> pets) {

        return new ToDoResponse(
            toDoInstanceDao.toDo().id(),
            toDoInstanceDao.toDo().tag() == null ? toDoInstanceDao.toDo().content()
                : toDoInstanceDao.toDo().tag().name(),
            pets,
            toDoInstanceDao.toDo().isAllDay(),
            toDoInstanceDao.date(),
            toDoInstanceDao.time(),
            toDoInstanceDao.toDo().isUsingAlarm(),
            toDoInstanceDao.toDo().color(),
            toDoInstanceDao.completeProfile() == null ? null
                : toDoInstanceDao.completeProfile().imageUrl(),
            toDoInstanceDao.completeTime()
        );
    }
}
